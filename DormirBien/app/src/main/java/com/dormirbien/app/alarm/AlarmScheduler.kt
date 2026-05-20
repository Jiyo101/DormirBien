package com.dormirbien.app.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

    /**
     * Schedules both alarms immediately when the user confirms the alarm time.
     * RC_MAIN fires at h1:m1, RC_BACKUP fires exactly 5 minutes later.
     * Both are independent AlarmManager alarms; neither creates the other at runtime.
     */
    fun schedule(ctx: Context, h1: Int, m1: Int, cycles: Int, hoursText: String) {
        val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Compute exact millisecond for main alarm (next occurrence of h1:m1)
        val cal1 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, h1); set(Calendar.MINUTE, m1)
            set(Calendar.SECOND, 0);       set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
        }
        val mainMillis   = cal1.timeInMillis
        val backupMillis = mainMillis + 5 * 60 * 1000L
        val cal2         = Calendar.getInstance().apply { timeInMillis = backupMillis }
        val h2 = cal2.get(Calendar.HOUR_OF_DAY)
        val m2 = cal2.get(Calendar.MINUTE)

        val mainPi   = makePi(ctx, RC_MAIN,   h1, m1, cycles, hoursText, isBackup = false)
        val backupPi = makePi(ctx, RC_BACKUP, h2, m2, cycles, hoursText, isBackup = true)

        scheduleExactMillis(ctx, am, mainMillis,   mainPi,   RC_MAIN)
        scheduleExactMillis(ctx, am, backupMillis, backupPi, RC_BACKUP)

        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("set",            true)
            .putBoolean("backup_pending", true)
            .putInt("h1", h1).putInt("m1", m1)
            .putInt("h2", h2).putInt("m2", m2)
            .putInt("cycles", cycles).putString("hours", hoursText)
            .putLong("main_millis",   mainMillis)
            .putLong("backup_millis", backupMillis)
            .apply()

        systemClock(ctx, h1, m1, "DormirBien · Despertar ($cycles ciclos)")
    }

    /** Cancels both alarms. Used when the user explicitly dismisses from the backup alarm
     *  or via the notification "Detener" button. */
    fun cancelAll(ctx: Context) {
        cancel(ctx, RC_MAIN)
        cancel(ctx, RC_BACKUP)
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("set",            false)
            .putBoolean("backup_pending", false)
            .apply()
    }

    /** Cancels only the main alarm, leaving the backup intact. */
    fun cancelMain(ctx: Context) {
        cancel(ctx, RC_MAIN)
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("set", false)
            .apply()
    }

    /** Cancels only the backup alarm. */
    fun cancelBackup(ctx: Context) {
        cancel(ctx, RC_BACKUP)
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("backup_pending", false)
            .apply()
    }

    /**
     * Re-registers pending alarms after device reboot.
     * Each alarm is only rescheduled if its target time is still in the future.
     */
    fun rescheduleAfterBoot(ctx: Context, p: SharedPreferences) {
        val am      = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val now     = System.currentTimeMillis()
        val cycles  = p.getInt("cycles", 0)
        val hours   = p.getString("hours", "") ?: ""

        if (p.getBoolean("set", false)) {
            val mainMillis = p.getLong("main_millis", 0L)
            val h1 = p.getInt("h1", 7); val m1 = p.getInt("m1", 0)
            if (mainMillis > now) {
                val pi = makePi(ctx, RC_MAIN, h1, m1, cycles, hours, isBackup = false)
                scheduleExactMillis(ctx, am, mainMillis, pi, RC_MAIN)
            } else {
                p.edit().putBoolean("set", false).apply()
            }
        }
        if (p.getBoolean("backup_pending", false)) {
            val backupMillis = p.getLong("backup_millis", 0L)
            val h2 = p.getInt("h2", 7); val m2 = p.getInt("m2", 5)
            if (backupMillis > now) {
                val pi = makePi(ctx, RC_BACKUP, h2, m2, cycles, hours, isBackup = true)
                scheduleExactMillis(ctx, am, backupMillis, pi, RC_BACKUP)
            } else {
                p.edit().putBoolean("backup_pending", false).apply()
            }
        }
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
