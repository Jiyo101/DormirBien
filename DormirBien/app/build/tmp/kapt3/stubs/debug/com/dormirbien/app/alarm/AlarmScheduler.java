package com.dormirbien.app.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.AlarmClock;
import java.util.Calendar;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\rH\u0002J\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J@\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\r2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002JH\u0010\u001e\u001a\n  *\u0004\u0018\u00010\u001f0\u001f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u000e\u0010!\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\rJ>\u0010#\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010$\u001a\u00020\r2\u0006\u0010%\u001a\u00020\r2\u0006\u0010&\u001a\u00020\r2\u0006\u0010\'\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u0004J.\u0010(\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010$\u001a\u00020\r2\u0006\u0010%\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\r2\u0006\u0010)\u001a\u00020\u0004J(\u0010*\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\r2\u0006\u0010+\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lcom/dormirbien/app/alarm/AlarmScheduler;", "", "()V", "ACTION_DISMISS", "", "ACTION_FIRE", "CHANNEL_ID", "EXTRA_BACKUP", "EXTRA_CYCLES", "EXTRA_H", "EXTRA_HOURS", "EXTRA_M", "NOTIF_ID", "", "RC_BACKUP", "RC_MAIN", "cancel", "", "ctx", "Landroid/content/Context;", "rc", "cancelAll", "cancelBackup", "exact", "h", "m", "cycles", "hours", "isBackup", "", "makePi", "Landroid/app/PendingIntent;", "kotlin.jvm.PlatformType", "pad", "n", "rescheduleAfterBoot", "h1", "m1", "h2", "m2", "schedule", "hoursText", "systemClock", "label", "app_debug"})
public final class AlarmScheduler {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_FIRE = "com.dormirbien.ALARM_FIRE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_DISMISS = "com.dormirbien.ALARM_DISMISS";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_H = "h";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_M = "m";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_CYCLES = "cycles";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_HOURS = "hours_txt";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_BACKUP = "is_backup";
    public static final int RC_MAIN = 100;
    public static final int RC_BACKUP = 101;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ID = "db_alarm_v3";
    public static final int NOTIF_ID = 42;
    @org.jetbrains.annotations.NotNull()
    public static final com.dormirbien.app.alarm.AlarmScheduler INSTANCE = null;
    
    private AlarmScheduler() {
        super();
    }
    
    public final void schedule(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx, int h1, int m1, int cycles, @org.jetbrains.annotations.NotNull()
    java.lang.String hoursText) {
    }
    
    public final void cancelAll(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
    }
    
    public final void cancelBackup(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
    }
    
    public final void rescheduleAfterBoot(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx, int h1, int m1, int h2, int m2, int cycles, @org.jetbrains.annotations.NotNull()
    java.lang.String hours) {
    }
    
    private final void exact(android.content.Context ctx, int h, int m, int cycles, java.lang.String hours, int rc, boolean isBackup) {
    }
    
    private final void cancel(android.content.Context ctx, int rc) {
    }
    
    private final android.app.PendingIntent makePi(android.content.Context ctx, int rc, int h, int m, int cycles, java.lang.String hours, boolean isBackup) {
        return null;
    }
    
    private final void systemClock(android.content.Context ctx, int h, int m, java.lang.String label) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String pad(int n) {
        return null;
    }
}