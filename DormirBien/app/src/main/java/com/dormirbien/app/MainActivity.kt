package com.dormirbien.app

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dormirbien.app.alarm.AlarmScheduler
import com.dormirbien.app.alarm.AlarmService
import com.dormirbien.app.data.local.AlarmPreferences
import com.dormirbien.app.data.repository.SleepRecord
import com.dormirbien.app.data.repository.SleepRepository
import com.dormirbien.app.ui.AppRoot
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var prefs:     AlarmPreferences
    @Inject lateinit var sleepRepo: SleepRepository

    private val showReview = mutableStateOf(false)
    private var soundPickedCallback: ((Uri) -> Unit)? = null

    private val notifLauncher   = registerForActivityResult(RequestPermission()) { askPermissions() }
    private val exactLauncher   = registerForActivityResult(StartActivityForResult()) { askPermissions() }
    private val overlayLauncher = registerForActivityResult(StartActivityForResult()) { askPermissions() }
    private val soundLauncher   = registerForActivityResult(StartActivityForResult()) { r ->
        val uri = r.data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
        if (uri != null) {
            lifecycleScope.launch { prefs.saveSoundUri(uri.toString()) }
            soundPickedCallback?.invoke(uri)
            Toast.makeText(this, "Sonido actualizado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ensureChannel()
        setContent {
            val review by showReview
            AppRoot(
                prefs             = prefs,
                showReview        = review,
                onReviewDismiss   = { showReview.value = false },
                onReviewSave      = { stars, feeling -> saveReview(stars, feeling) },
                onScheduleAlarms  = { h1, m1, cycles, ht -> scheduleAlarms(h1, m1, cycles, ht) },
                onCancelAlarms    = { cancelAlarms() },
                onCancelBackup    = { cancelBackup() },
                onOpenSoundPicker = { cb -> soundPickedCallback = cb; openSoundPicker() },
            )
        }
        window.decorView.post { askPermissions() }
    }

    override fun onResume() {
        super.onResume()
        if (prefs.isPendingReview()) {
            prefs.clearPendingReview()
            window.decorView.postDelayed({ showReview.value = true }, 400)
        }
    }

    // ── Alarm operations ──────────────────────────────────────────────────────

    private fun scheduleAlarms(h1: Int, m1: Int, cycles: Int, hoursText: String) {
        val h2 = (h1 * 60 + m1 + 5) / 60 % 24
        val m2 = (h1 * 60 + m1 + 5) % 60
        AlarmScheduler.schedule(this, h1, m1, cycles, hoursText)
        lifecycleScope.launch {
            prefs.saveAlarm(h1, m1, h2, m2, cycles, hoursText)
            val key = todayKey()
            val existing = sleepRepo.getByKey(key)
            sleepRepo.upsert(SleepRecord(key, cycles * 90f / 60f,
                existing?.stars ?: 0, existing?.feeling ?: ""))
        }
        Toast.makeText(this,
            "✅ Alarmas: ${AlarmScheduler.pad(h1)}:${AlarmScheduler.pad(m1)}" +
            " y ${AlarmScheduler.pad(h2)}:${AlarmScheduler.pad(m2)}",
            Toast.LENGTH_LONG).show()
    }

    private fun cancelAlarms() {
        AlarmScheduler.cancelAll(this)
        stopService(Intent(this, AlarmService::class.java))
        lifecycleScope.launch { prefs.clearAlarm() }
    }

    private fun cancelBackup() {
        AlarmScheduler.cancelBackup(this)
        Toast.makeText(this, "Recordatorio +5 min cancelado", Toast.LENGTH_SHORT).show()
    }

    private fun saveReview(stars: Int, feeling: String) {
        lifecycleScope.launch {
            val key = todayKey()
            val existing = sleepRepo.getByKey(key)
            sleepRepo.upsert(SleepRecord(key, existing?.hours ?: 0f, stars, feeling))
        }
    }

    private fun openSoundPicker() {
        soundLauncher.launch(Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                RingtoneManager.TYPE_ALARM or RingtoneManager.TYPE_RINGTONE)
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Sonido de alarma")
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT,  false)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        })
    }

    // ── Permission chain ─────────────────────────────────────────────────────

    private fun askPermissions() {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // 1. POST_NOTIFICATIONS (Android 13+)
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            dialog("Permiso de notificaciones",
                "DormirBien necesita enviarte la alarma como notificación para " +
                "que aparezca en la pantalla de bloqueo.") {
                notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            return
        }

        // 2. SCHEDULE_EXACT_ALARM (Android 12+)
        if (Build.VERSION.SDK_INT >= 31) {
            val am = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!am.canScheduleExactAlarms()) {
                dialog("Alarma exacta",
                    "Para que la alarma suene a la hora exacta incluso en modo " +
                    "ahorro de batería.") {
                    exactLauncher.launch(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                }
                return
            }
        }

        // 3. SYSTEM_ALERT_WINDOW
        //    Needed so AlarmActivity can appear over the lock screen.
        //    On Xiaomi/MIUI this is the primary mechanism.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            dialog("Mostrar sobre otras apps",
                "Para que la interfaz de alarma aparezca sobre la pantalla de " +
                "bloqueo, DormirBien necesita el permiso «Mostrar sobre otras apps».\n\n" +
                "Se abrirá la configuración del sistema. Busca DormirBien y actívalo.") {
                overlayLauncher.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")))
            }
            return
        }

        // 4. DND bypass
        if (Build.VERSION.SDK_INT >= 23 && !nm.isNotificationPolicyAccessGranted) {
            dialog("Modo No Molestar",
                "Para que la alarma suene aunque el móvil esté en silencio " +
                "o modo No Molestar.") {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            return
        }

        // 5. Xiaomi-specific: show a one-time guide for MIUI lock screen settings
        //    These CANNOT be granted programmatically — the user must do it manually.
        val sp = getSharedPreferences("db_sync", MODE_PRIVATE)
        if (!sp.getBoolean("miui_guide_shown", false) && isMiui()) {
            sp.edit().putBoolean("miui_guide_shown", true).apply()
            AlertDialog.Builder(this)
                .setTitle("⚠️ Configuración extra para Xiaomi")
                .setMessage(
                    "Para que la pantalla de alarma aparezca cuando el móvil está bloqueado, " +
                    "necesitas activar DOS ajustes manualmente en MIUI:\n\n" +
                    "1️⃣  Ajustes → Apps → DormirBien → Permisos →\n" +
                    "    «Mostrar ventana emergente» → Activar\n\n" +
                    "2️⃣  Ajustes → Apps → DormirBien → Permisos →\n" +
                    "    «Mostrar en pantalla de bloqueo» → Activar\n\n" +
                    "Sin estos dos ajustes, MIUI bloquea que cualquier app " +
                    "muestre pantallas sobre el bloqueo."
                )
                .setPositiveButton("Ir a ajustes de DormirBien") { _, _ ->
                    try {
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:$packageName")))
                    } catch (_: Exception) {
                        startActivity(Intent(Settings.ACTION_APPLICATION_SETTINGS))
                    }
                }
                .setNegativeButton("Entendido", null)
                .show()
        }
    }

    private fun isMiui(): Boolean = try {
        val cls = Class.forName("android.os.SystemProperties")
        val get = cls.getMethod("get", String::class.java)
        val prop = get.invoke(null, "ro.miui.ui.version.name") as? String
        !prop.isNullOrEmpty()
    } catch (_: Exception) { false }

    private fun dialog(title: String, msg: String, onOk: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title).setMessage(msg)
            .setPositiveButton("Ir a configuración") { _, _ -> onOk() }
            .setNegativeButton("Ahora no", null)
            .show()
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (nm.getNotificationChannel(AlarmScheduler.CHANNEL_ID) != null) return
        nm.createNotificationChannel(NotificationChannel(
            AlarmScheduler.CHANNEL_ID, "Alarmas DormirBien",
            NotificationManager.IMPORTANCE_HIGH).apply {
            setBypassDnd(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            enableVibration(true)
            enableLights(true)
        })
    }

    private fun todayKey() =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}
