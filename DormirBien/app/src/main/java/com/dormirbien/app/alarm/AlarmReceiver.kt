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
            }
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED -> {
                val p = ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE)
                if (!p.getBoolean("set", false)) return
                AlarmScheduler.rescheduleAfterBoot(ctx,
                    p.getInt("h1", 7), p.getInt("m1", 0),
                    p.getInt("h2", 7), p.getInt("m2", 5),
                    p.getInt("cycles", 0), p.getString("hours", "") ?: "")
            }
        }
    }
}
