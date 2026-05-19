package com.dormirbien.app.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        when (intent.action) {
            AlarmScheduler.ACTION_FIRE -> {
                val isBackup = intent.getBooleanExtra(AlarmScheduler.EXTRA_BACKUP, false)
                if (!isBackup) {
                    // Main alarm fired: schedule backup for exactly 5 minutes from now
                    val cycles = intent.getIntExtra(AlarmScheduler.EXTRA_CYCLES, 0)
                    val hours  = intent.getStringExtra(AlarmScheduler.EXTRA_HOURS) ?: ""
                    AlarmScheduler.scheduleBackup(ctx, cycles, hours)
                } else {
                    // Backup alarm fired: mark it consumed
                    ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
                        .putBoolean("backup_pending", false)
                        .apply()
                }
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
                    if (p.getBoolean("set", false)) {
                        AlarmScheduler.rescheduleAfterBoot(ctx,
                            p.getInt("h1", 7), p.getInt("m1", 0),
                            p.getInt("cycles", 0), p.getString("hours", "") ?: "")
                    }
                    if (p.getBoolean("backup_pending", false)) {
                        val backupMillis = p.getLong("backup_millis", 0L)
                        if (backupMillis > System.currentTimeMillis()) {
                            AlarmScheduler.rescheduleBackupMillis(ctx, backupMillis,
                                p.getInt("cycles", 0), p.getString("hours", "") ?: "")
                        } else {
                            p.edit().putBoolean("backup_pending", false).apply()
                        }
                    }
                } catch (e: Exception) {
                    // Credential storage locked (Direct Boot). Alarm rescheduled on BOOT_COMPLETED.
                }
            }
        }
    }
}
