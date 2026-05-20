package com.dormirbien.app.alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.*
import android.net.Uri
import android.os.*
import androidx.core.app.NotificationCompat

/**
 * Foreground service that:
 *
 * PATH A — Standard Android (stock ROMs):
 *   setFullScreenIntent(highPriority=true) causes the OS to launch AlarmActivity
 *   over the lock screen automatically. Works on Pixel, Samsung (stock), OnePlus, etc.
 *
 * PATH B — Xiaomi/MIUI:
 *   MIUI silently ignores fullScreenIntent. The only working approach is:
 *   a) SYSTEM_ALERT_WINDOW granted by user (shows in Settings > Apps > DormirBien)
 *   b) "Mostrar ventana emergente en segundo plano" enabled in MIUI app settings
 *   c) "Mostrar en pantalla de bloqueo" enabled in MIUI app settings
 *   With those active, startActivity() from a foreground service works.
 *
 * Both paths are attempted simultaneously for maximum compatibility.
 */
class AlarmService : Service() {
    private var player:   MediaPlayer? = null
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) { stopSelf(); return START_NOT_STICKY }

        // Release any resources from a previous onStartCommand call.
        // This can happen if the backup alarm fires while the main alarm is still ringing.
        try { player?.stop(); player?.release() } catch (e: Exception) {}
        player = null
        try { wakeLock?.release() } catch (e: Exception) {}
        wakeLock = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                (getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager).cancel()
            else @Suppress("DEPRECATION")
                (getSystemService(VIBRATOR_SERVICE) as Vibrator).cancel()
        } catch (e: Exception) {}

        val h        = intent.getIntExtra(AlarmScheduler.EXTRA_H,      7)
        val m        = intent.getIntExtra(AlarmScheduler.EXTRA_M,      0)
        val cycles   = intent.getIntExtra(AlarmScheduler.EXTRA_CYCLES, 0)
        val hours    = intent.getStringExtra(AlarmScheduler.EXTRA_HOURS)   ?: ""
        val isBackup = intent.getBooleanExtra(AlarmScheduler.EXTRA_BACKUP, false)

        // Mark pending review for MainActivity.onResume()
        getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("pending_review", true)
            .putBoolean("set", false)
            .apply()

        // Wake screen and keep CPU alive
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        @Suppress("DEPRECATION")
        wakeLock = pm.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "DormirBien:Wake"
        )
        wakeLock?.acquire(120_000L)

        ensureChannel()

        // Build AlarmActivity intent
        val actIntent = Intent(this, AlarmActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK     or
                Intent.FLAG_ACTIVITY_CLEAR_TOP    or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
            putExtra(AlarmScheduler.EXTRA_H,      h)
            putExtra(AlarmScheduler.EXTRA_M,      m)
            putExtra(AlarmScheduler.EXTRA_CYCLES, cycles)
            putExtra(AlarmScheduler.EXTRA_HOURS,  hours)
            putExtra(AlarmScheduler.EXTRA_BACKUP, isBackup)
        }

        val fsPi = PendingIntent.getActivity(this, 0, actIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val dismissPi = PendingIntent.getBroadcast(this, 99,
            Intent(this, AlarmReceiver::class.java).apply { action = AlarmScheduler.ACTION_DISMISS },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val hStr  = AlarmScheduler.pad(h)
        val mStr  = AlarmScheduler.pad(m)
        val title = if (isBackup) "⏰ Recordatorio — $hStr:$mStr"
                    else          "⏰ ¡Buenos días! Son las $hStr:$mStr"
        val body  = if (isBackup) "Toca para detener" else "$cycles ciclos · $hours"

        val notif = NotificationCompat.Builder(this, AlarmScheduler.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            // PATH A: standard Android — OS shows AlarmActivity over lock screen
            .setFullScreenIntent(fsPi, true)
            .setContentIntent(fsPi)
            .addAction(android.R.drawable.ic_delete, "Detener", dismissPi)
            .build()

        // startForeground MUST come before startActivity on Android 10+
        startForeground(AlarmScheduler.NOTIF_ID, notif)

        // PATH B: Xiaomi/MIUI — direct startActivity() from foreground service
        // Works when SYSTEM_ALERT_WINDOW + MIUI "popup" permissions are granted
        try {
            startActivity(actIntent)
        } catch (e: Exception) {
            android.util.Log.w("DormirBien", "startActivity failed (MIUI perms missing?): ${e.message}")
        }

        playSound()
        vibrate()
        return START_NOT_STICKY
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (nm.getNotificationChannel(AlarmScheduler.CHANNEL_ID) != null) return
        nm.createNotificationChannel(NotificationChannel(
            AlarmScheduler.CHANNEL_ID, "Alarmas DormirBien",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setBypassDnd(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setSound(null, null)  // MediaPlayer handles audio; no channel sound interference
            enableVibration(true)
            enableLights(true)
            lightColor = Color.parseColor("#7aaeff")
        })
    }

    private fun playSound() {
        try {
            val am = getSystemService(AUDIO_SERVICE) as AudioManager
            am.setStreamVolume(AudioManager.STREAM_ALARM,
                am.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0)
            val customUri = getSharedPreferences("db_sync", Context.MODE_PRIVATE)
                .getString("sound_uri", null)
            val uri: Uri = if (!customUri.isNullOrEmpty()) Uri.parse(customUri)
                else RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                    ?: throw Exception("No alarm sound URI available")
            player = MediaPlayer().apply {
                setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .build())
                setDataSource(this@AlarmService, uri)
                isLooping = true
                prepare()
                start()
            }
        } catch (e: Exception) {
            try {
                val r = RingtoneManager.getRingtone(this,
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                if (Build.VERSION.SDK_INT >= 28) r?.isLooping = true
                r?.play()
            } catch (e: Exception) {}
        }
    }

    private fun vibrate() {
        val pat = longArrayOf(0, 700, 300, 700, 300, 1400)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                (getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager)
                    .defaultVibrator.vibrate(VibrationEffect.createWaveform(pat, 0))
            else {
                @Suppress("DEPRECATION")
                val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= 26)
                    v.vibrate(VibrationEffect.createWaveform(pat, 0))
                else @Suppress("DEPRECATION") v.vibrate(pat, 0)
            }
        } catch (e: Exception) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        try { player?.stop(); player?.release() } catch (e: Exception) {}
        player = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                (getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager).cancel()
            else @Suppress("DEPRECATION")
                (getSystemService(VIBRATOR_SERVICE) as Vibrator).cancel()
        } catch (e: Exception) {}
        try { wakeLock?.release() } catch (e: Exception) {}
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
}
