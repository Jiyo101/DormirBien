package com.dormirbien.app.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.AlarmClock
import java.util.Calendar

object AlarmScheduler {
    const val ACTION_FIRE    = "com.dormirbien.ALARM_FIRE"
    const val ACTION_DISMISS = "com.dormirbien.ALARM_DISMISS"
    const val EXTRA_H        = "h"
    const val EXTRA_M        = "m"
    const val EXTRA_CYCLES   = "cycles"
    const val EXTRA_HOURS    = "hours_txt"
    const val EXTRA_BACKUP   = "is_backup"
    const val RC_MAIN        = 100
    const val RC_BACKUP      = 101
    const val CHANNEL_ID     = "db_alarm_v4"
    const val NOTIF_ID       = 42

    // Only schedules the main alarm. The backup is scheduled dynamically when the main fires.
    fun schedule(ctx: Context, h1: Int, m1: Int, cycles: Int, hoursText: String) {
        exact(ctx, h1, m1, cycles, hoursText, RC_MAIN, isBackup = false)
        systemClock(ctx, h1, m1, "DormirBien · Despertar ($cycles ciclos)")
    }

    // Called from AlarmReceiver when the main alarm fires. Schedules backup for exactly +5 min.
    fun scheduleBackup(ctx: Context, cycles: Int, hoursText: String) {
        val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val backupMillis = System.currentTimeMillis() + 5 * 60 * 1000L
        val cal = Calendar.getInstance().apply { timeInMillis = backupMillis }
        val h2 = cal.get(Calendar.HOUR_OF_DAY)
        val m2 = cal.get(Calendar.MINUTE)
        val pi = makePi(ctx, RC_BACKUP, h2, m2, cycles, hoursText, isBackup = true)
        scheduleExactMillis(ctx, am, backupMillis, pi, RC_BACKUP)
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("backup_pending", true)
            .putLong("backup_millis", backupMillis)
            .putInt("h2", h2).putInt("m2", m2)
            .apply()
    }

    fun cancelAll(ctx: Context) {
        cancel(ctx, RC_MAIN)
        cancel(ctx, RC_BACKUP)
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("backup_pending", false)
            .apply()
    }

    fun cancelBackup(ctx: Context) {
        cancel(ctx, RC_BACKUP)
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("backup_pending", false)
            .apply()
    }

    fun rescheduleAfterBoot(ctx: Context, h1: Int, m1: Int, cycles: Int, hoursText: String) {
        exact(ctx, h1, m1, cycles, hoursText, RC_MAIN, false)
    }

    fun rescheduleBackupMillis(ctx: Context, backupMillis: Long, cycles: Int, hoursText: String) {
        val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val cal = Calendar.getInstance().apply { timeInMillis = backupMillis }
        val h2 = cal.get(Calendar.HOUR_OF_DAY)
        val m2 = cal.get(Calendar.MINUTE)
        val pi = makePi(ctx, RC_BACKUP, h2, m2, cycles, hoursText, isBackup = true)
        scheduleExactMillis(ctx, am, backupMillis, pi, RC_BACKUP)
    }

    private fun exact(ctx: Context, h: Int, m: Int, cycles: Int, hours: String,
                       rc: Int, isBackup: Boolean) {
        val am  = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, h); set(Calendar.MINUTE, m)
            set(Calendar.SECOND, 0);      set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
        }
        val pi = makePi(ctx, rc, h, m, cycles, hours, isBackup)
        scheduleExactMillis(ctx, am, cal.timeInMillis, pi, rc)
    }

    private fun scheduleExactMillis(ctx: Context, am: AlarmManager, millis: Long,
                                     pi: PendingIntent, rc: Int) {
        try {
            when {
                Build.VERSION.SDK_INT >= 31 && am.canScheduleExactAlarms() -> {
                    val launchIntent = ctx.packageManager
                        .getLaunchIntentForPackage(ctx.packageName)
                        ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        ?: Intent().setPackage(ctx.packageName)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val showPi = PendingIntent.getActivity(
                        ctx, rc + 200, launchIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                    am.setAlarmClock(AlarmManager.AlarmClockInfo(millis, showPi), pi)
                }
                Build.VERSION.SDK_INT >= 23 ->
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pi)
                else -> am.setExact(AlarmManager.RTC_WAKEUP, millis, pi)
            }
        } catch (e: Exception) { am.set(AlarmManager.RTC_WAKEUP, millis, pi) }
    }

    private fun cancel(ctx: Context, rc: Int) {
        val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = PendingIntent.getBroadcast(ctx, rc,
            Intent(ctx, AlarmReceiver::class.java).apply { action = ACTION_FIRE },
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE) ?: return
        am.cancel(pi); pi.cancel()
    }

    private fun makePi(ctx: Context, rc: Int, h: Int, m: Int, cycles: Int,
                        hours: String, isBackup: Boolean) =
        PendingIntent.getBroadcast(ctx, rc,
            Intent(ctx, AlarmReceiver::class.java).apply {
                action = ACTION_FIRE
                putExtra(EXTRA_H, h); putExtra(EXTRA_M, m)
                putExtra(EXTRA_CYCLES, cycles); putExtra(EXTRA_HOURS, hours)
                putExtra(EXTRA_BACKUP, isBackup)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    private fun systemClock(ctx: Context, h: Int, m: Int, label: String) {
        try {
            ctx.startActivity(Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_HOUR, h)
                putExtra(AlarmClock.EXTRA_MINUTES, m)
                putExtra(AlarmClock.EXTRA_MESSAGE, label)
                putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (e: Exception) {}
    }

    fun pad(n: Int) = n.toString().padStart(2, '0')
}
