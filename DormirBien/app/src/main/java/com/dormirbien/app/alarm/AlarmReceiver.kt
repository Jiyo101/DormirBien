package com.dormirbien.app.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        when (intent.action) {
            AlarmScheduler.ACTION_FIRE -> {
                val svc = Intent(ctx, AlarmService::class.java).apply { putExtras(intent) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    ctx.startForegroundService(svc)
                else ctx.startService(svc)
            }
            AlarmScheduler.ACTION_DISMISS -> {
                ctx.stopService(Intent(ctx, AlarmService::class.java))
                AlarmScheduler.cancelAll(ctx)
                ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
                    .putBoolean("set", false)
                    .putBoolean("pending_review", true)
                    .apply()
                // Notify AlarmActivity (if it's on screen) to finish.
                // The dismissPi in AlarmService targets this receiver explicitly,
                // so AlarmActivity's dynamically-registered dismissReceiver never
                // sees the original broadcast — this implicit package-scoped broadcast
                // reaches it on API 26+ (dynamic receivers are exempt from the
                // implicit-broadcast restriction that blocks manifest receivers).
                ctx.sendBroadcast(
                    Intent(AlarmScheduler.ACTION_DISMISS)
                        .setPackage(ctx.packageName)
                        .addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY)
                )
            }
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED -> {
                // LOCKED_BOOT_COMPLETED fires before the user unlocks the device.
                // Credential-encrypted SharedPreferences (MODE_PRIVATE) may be inaccessible
                // in Direct Boot mode and throw on strict implementations.
                // If that happens, BOOT_COMPLETED (fires after first unlock) will retry.
                try {
                    val p = ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE)
                    if (!p.getBoolean("set", false)) return
                    AlarmScheduler.rescheduleAfterBoot(ctx,
                        p.getInt("h1", 7), p.getInt("m1", 0),
                        p.getInt("h2", 7), p.getInt("m2", 5),
                        p.getInt("cycles", 0), p.getString("hours", "") ?: "")
                } catch (e: Exception) {
                    // Credential storage locked (Direct Boot). Alarm rescheduled on BOOT_COMPLETED.
                }
            }
        }
    }
}
