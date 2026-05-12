package com.dormirbien.app.alarm;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.*;
import android.net.Uri;
import android.os.*;
import androidx.core.app.NotificationCompat;

/**
 * Foreground service that:
 *
 * PATH A — Standard Android (stock ROMs):
 *  setFullScreenIntent(highPriority=true) causes the OS to launch AlarmActivity
 *  over the lock screen automatically. Works on Pixel, Samsung (stock), OnePlus, etc.
 *
 * PATH B — Xiaomi/MIUI:
 *  MIUI silently ignores fullScreenIntent. The only working approach is:
 *  a) SYSTEM_ALERT_WINDOW granted by user (shows in Settings > Apps > DormirBien)
 *  b) "Mostrar ventana emergente en segundo plano" enabled in MIUI app settings
 *  c) "Mostrar en pantalla de bloqueo" enabled in MIUI app settings
 *  With those active, startActivity() from a foreground service works.
 *
 * Both paths are attempted simultaneously for maximum compatibility.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\b\u001a\u00020\tH\u0002J\u0014\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016J\b\u0010\u000e\u001a\u00020\tH\u0016J\"\u0010\u000f\u001a\u00020\u00102\b\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\b\u0010\u0013\u001a\u00020\tH\u0002J\b\u0010\u0014\u001a\u00020\tH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0018\u00010\u0006R\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/dormirbien/app/alarm/AlarmService;", "Landroid/app/Service;", "()V", "player", "Landroid/media/MediaPlayer;", "wakeLock", "Landroid/os/PowerManager$WakeLock;", "Landroid/os/PowerManager;", "ensureChannel", "", "onBind", "", "intent", "Landroid/content/Intent;", "onDestroy", "onStartCommand", "", "flags", "startId", "playSound", "vibrate", "app_debug"})
public final class AlarmService extends android.app.Service {
    @org.jetbrains.annotations.Nullable()
    private android.media.MediaPlayer player;
    @org.jetbrains.annotations.Nullable()
    private android.os.PowerManager.WakeLock wakeLock;
    
    public AlarmService() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Void onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void ensureChannel() {
    }
    
    private final void playSound() {
    }
    
    private final void vibrate() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
}