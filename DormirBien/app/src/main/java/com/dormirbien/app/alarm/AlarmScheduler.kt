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
    const val CHANNEL_ID     = "db_alarm_v3"
    const val NOTIF_ID       = 42

    fun schedule(ctx: Context, h1: Int, m1: Int, cycles: Int, hoursText: String) {
        val t  = h1 * 60 + m1 + 5
        val h2 = (t / 60) % 24
        val m2 = t % 60

        exact(ctx, h1, m1, cycles, hoursText, RC_MAIN,   isBackup = false)
        exact(ctx, h2, m2, cycles, hoursText, RC_BACKUP, isBackup = true)

        // Also create alarms in system Clock app (visible in Reloj)
        systemClock(ctx, h1, m1, "DormirBien · Despertar ($cycles ciclos)")
        systemClock(ctx, h2, m2, "DormirBien · Recordatorio +5 min")
    }

    fun cancelAll(ctx: Context) { cancel(ctx, RC_MAIN); cancel(ctx, RC_BACKUP) }
    fun cancelBackup(ctx: Context) { cancel(ctx, RC_BACKUP) }

    fun rescheduleAfterBoot(ctx: Context, h1: Int, m1: Int, h2: Int, m2: Int,
                             cycles: Int, hours: String) {
        exact(ctx, h1, m1, cycles, hours, RC_MAIN,   false)
        exact(ctx, h2, m2, cycles, hours, RC_BACKUP, true)
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
        try {
            when {
                Build.VERSION.SDK_INT >= 31 && am.canScheduleExactAlarms() ->
                    am.setAlarmClock(AlarmManager.AlarmClockInfo(cal.timeInMillis, pi), pi)
                Build.VERSION.SDK_INT >= 23 ->
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
                else -> am.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
            }
        } catch (_: Exception) { am.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi) }
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
        } catch (_: Exception) {}
    }

    fun pad(n: Int) = n.toString().padStart(2, '0')
}
