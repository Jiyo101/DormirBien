package com.dormirbien.app.alarm

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*
import kotlin.random.Random

/**
 * Full-screen activity shown over the lock screen.
 *
 * THE CRITICAL RULES for lock screen display (especially Xiaomi/MIUI):
 *
 * 1. Use ComponentActivity, NOT AppCompatActivity
 *    AppCompatActivity can interfere with window flag timing.
 *
 * 2. Window flags BEFORE super.onCreate() — MANDATORY ORDER:
 *    a. setShowWhenLocked(true) + setTurnScreenOn(true)  [API 27+]
 *    b. window.addFlags(FLAG_SHOW_WHEN_LOCKED | ...)     [all APIs]
 *    c. super.onCreate(savedInstanceState)               ← AFTER flags
 *
 * 3. android:exported="true" in manifest — system must be able to launch it
 *
 * 4. android:showWhenLocked + android:turnScreenOn in manifest — fallback
 *
 * 5. AlarmService calls startActivity() directly after startForeground()
 *    This is the MIUI-compatible path (requires SYSTEM_ALERT_WINDOW)
 */
class AlarmActivity : ComponentActivity() {

    private var alarmH        by mutableStateOf(7)
    private var alarmM        by mutableStateOf(0)
    private var alarmCycles   by mutableStateOf(0)
    private var alarmHours    by mutableStateOf("")
    private var alarmIsBackup by mutableStateOf(false)

    private val dismissReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context, i: Intent) {
            if (i.action == AlarmScheduler.ACTION_DISMISS) finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // ╔══════════════════════════════════════════════════════════╗
        // ║  STEP 1 — Flags BEFORE super.onCreate()                 ║
        // ║  This is the single most important thing for MIUI.       ║
        // ╚══════════════════════════════════════════════════════════╝
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        @Suppress("DEPRECATION")
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED      or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON        or
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON        or
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD      or
            WindowManager.LayoutParams.FLAG_FULLSCREEN            or
            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )

        // ╔══════════════════════════════════════════════════════════╗
        // ║  STEP 2 — super.onCreate() AFTER flags                  ║
        // ╚══════════════════════════════════════════════════════════╝
        super.onCreate(savedInstanceState)

        // STEP 3 — Dismiss keyguard (works with PIN/pattern lock)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (getSystemService(KEYGUARD_SERVICE) as KeyguardManager)
                .requestDismissKeyguard(this, null)
        }

        readIntent(intent)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { /* blocked — must use button or swipe */ }
        })

        val filter = IntentFilter(AlarmScheduler.ACTION_DISMISS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            registerReceiver(dismissReceiver, filter, RECEIVER_NOT_EXPORTED)
        else @Suppress("UnspecifiedRegisterReceiverFlag")
            registerReceiver(dismissReceiver, filter)

        setContent {
            AlarmScreen(
                h        = alarmH, m = alarmM, cycles = alarmCycles,
                hours    = alarmHours, isBackup = alarmIsBackup,
                onStop   = { stopAlarm() },
                onCancel = { AlarmScheduler.cancelBackup(this); stopAlarm() },
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        readIntent(intent)
    }

    private fun readIntent(i: Intent) {
        alarmH        = i.getIntExtra(AlarmScheduler.EXTRA_H,      7)
        alarmM        = i.getIntExtra(AlarmScheduler.EXTRA_M,      0)
        alarmCycles   = i.getIntExtra(AlarmScheduler.EXTRA_CYCLES, 0)
        alarmHours    = i.getStringExtra(AlarmScheduler.EXTRA_HOURS) ?: ""
        alarmIsBackup = i.getBooleanExtra(AlarmScheduler.EXTRA_BACKUP, false)
    }

    private fun stopAlarm() {
        stopService(Intent(this, AlarmService::class.java))
        AlarmScheduler.cancelAll(this)
        getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("set",            false)
            .putBoolean("pending_review", true)
            .apply()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(dismissReceiver) } catch (e: Exception) {}
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Compose lock screen UI — matches the reference photo:
//  dark bg + stars + big time + white card + swipe up hint
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AlarmScreen(
    h: Int, m: Int, cycles: Int, hours: String, isBackup: Boolean,
    onStop: () -> Unit, onCancel: () -> Unit,
) {
    var drag by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF06090F))
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart    = { drag = 0f },
                    onVerticalDrag = { _, delta ->
                        drag += delta
                        if (drag < -180f) onStop()
                    }
                )
            }
    ) {
        StarField()

        Column(
            modifier              = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            horizontalAlignment   = Alignment.CenterHorizontally,
            verticalArrangement   = Arrangement.SpaceBetween,
        ) {
            Spacer(Modifier.height(60.dp))

            // ── Date + big time ───────────────────────────────────────────
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                var dateStr by remember { mutableStateOf(SimpleDateFormat("dd MMM EEEE", Locale("es","ES")).format(Date())) }
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(60_000)
                        dateStr = SimpleDateFormat("dd MMM EEEE", Locale("es","ES")).format(Date())
                    }
                }
                Text(dateStr, color = Color(0xFFCCDDEE), fontSize = 15.sp, letterSpacing = 0.3.sp)
                Spacer(Modifier.height(10.dp))
                Text(
                    "${AlarmScheduler.pad(h)}:${AlarmScheduler.pad(m)}",
                    color = Color.White, fontSize = 84.sp,
                    fontWeight = FontWeight.Light, letterSpacing = 2.sp, lineHeight = 84.sp,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = when {
                        isBackup   -> "Recordatorio automático +5 min"
                        cycles > 0 -> "$cycles ciclos completados · $hours"
                        else       -> "DormirBien"
                    },
                    color = Color(0xFF8899BB), fontSize = 13.sp, textAlign = TextAlign.Center,
                )
            }

            // ── Action cards ──────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // White dismiss card — same visual as "Posponer" in the reference photo
                Surface(
                    onClick         = onStop,
                    color           = Color.White,
                    shape           = RoundedCornerShape(18.dp),
                    shadowElevation = 6.dp,
                    modifier        = Modifier.fillMaxWidth(0.80f).height(70.dp),
                ) {
                    Row(
                        verticalAlignment      = Alignment.CenterVertically,
                        horizontalArrangement  = Arrangement.Center,
                        modifier               = Modifier.fillMaxSize(),
                    ) {
                        Text("🛑", fontSize = 22.sp)
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text       = if (isBackup) "Detener recordatorio" else "Detener alarma",
                            color      = Color(0xFF111111),
                            fontSize   = 17.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                // Cancel backup alarm — only relevant when the main alarm is ringing
                if (!isBackup) {
                    OutlinedButton(
                        onClick  = onCancel,
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF6B6B)),
                        border   = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF6B6B).copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth(0.72f).height(46.dp),
                    ) {
                        Text("✕  Cancelar recordatorio +5 min", fontSize = 13.sp)
                    }
                }
            }

            // ── Bouncing swipe hint ───────────────────────────────────────
            BouncingHint()
            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun BouncingHint() {
    val offset by rememberInfiniteTransition(label = "hint").animateFloat(
        initialValue  = 0f, targetValue = -14f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "bounce",
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier            = Modifier.offset(y = offset.dp),
    ) {
        Text("∧", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Light)
        Spacer(Modifier.height(4.dp))
        Text(
            "Desliza hacia arriba para detener",
            color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun StarField() {
    data class Star(val x: Float, val y: Float, val r: Float, val phase: Float)
    val stars = remember {
        val rng = Random(42L)
        List(70) { Star(rng.nextFloat(), rng.nextFloat() * 0.70f,
            rng.nextFloat() * 1.7f + 0.3f, rng.nextFloat() * (2f * PI.toFloat())) }
    }
    val tick by rememberInfiniteTransition(label = "stars").animateFloat(
        initialValue = 0f, targetValue = (2f * PI.toFloat()),
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing)),
        label = "tick",
    )
    Canvas(Modifier.fillMaxSize()) {
        val w = size.width; val h = size.height
        stars.forEach { s ->
            val a = (0.08f + 0.78f * ((sin(tick + s.phase) + 1f) / 2f)).coerceIn(0.04f, 0.92f)
            drawCircle(Color.White.copy(alpha = a), s.r, Offset(s.x * w, s.y * h))
        }
    }
}
