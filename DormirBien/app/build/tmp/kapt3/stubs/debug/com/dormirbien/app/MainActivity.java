package com.dormirbien.app;

import android.Manifest;
import android.app.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.compose.runtime.*;
import androidx.core.content.ContextCompat;
import com.dormirbien.app.alarm.AlarmScheduler;
import com.dormirbien.app.alarm.AlarmService;
import com.dormirbien.app.data.local.AlarmPreferences;
import com.dormirbien.app.data.repository.SleepRecord;
import com.dormirbien.app.data.repository.SleepRepository;
import dagger.hilt.android.AndroidEntryPoint;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001e\u001a\u00020\u001dH\u0002J\b\u0010\u001f\u001a\u00020\u001dH\u0002J\b\u0010 \u001a\u00020\u001dH\u0002J&\u0010!\u001a\u00020\u001d2\u0006\u0010\"\u001a\u00020\b2\u0006\u0010#\u001a\u00020\b2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001d0%H\u0002J\b\u0010&\u001a\u00020\u001dH\u0002J\u0012\u0010\'\u001a\u00020\u001d2\b\u0010(\u001a\u0004\u0018\u00010)H\u0014J\b\u0010*\u001a\u00020\u001dH\u0014J\b\u0010+\u001a\u00020\u001dH\u0002J\u0018\u0010,\u001a\u00020\u001d2\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020\bH\u0002J(\u00100\u001a\u00020\u001d2\u0006\u00101\u001a\u00020.2\u0006\u00102\u001a\u00020.2\u0006\u00103\u001a\u00020.2\u0006\u00104\u001a\u00020\bH\u0002J\u0010\u00105\u001a\n \u0006*\u0004\u0018\u00010\b0\bH\u0002R\u001c\u0010\u0003\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\b0\b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\t\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\u00020\u000b8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0013\u001a\u00020\u00148\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001c\u0010\u0019\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001a\u001a\u0010\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001d\u0018\u00010\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00066"}, d2 = {"Lcom/dormirbien/app/MainActivity;", "Landroidx/activity/ComponentActivity;", "()V", "exactLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "kotlin.jvm.PlatformType", "notifLauncher", "", "overlayLauncher", "prefs", "Lcom/dormirbien/app/data/local/AlarmPreferences;", "getPrefs", "()Lcom/dormirbien/app/data/local/AlarmPreferences;", "setPrefs", "(Lcom/dormirbien/app/data/local/AlarmPreferences;)V", "showReview", "Landroidx/compose/runtime/MutableState;", "", "sleepRepo", "Lcom/dormirbien/app/data/repository/SleepRepository;", "getSleepRepo", "()Lcom/dormirbien/app/data/repository/SleepRepository;", "setSleepRepo", "(Lcom/dormirbien/app/data/repository/SleepRepository;)V", "soundLauncher", "soundPickedCallback", "Lkotlin/Function1;", "Landroid/net/Uri;", "", "askPermissions", "cancelAlarms", "cancelBackup", "dialog", "title", "msg", "onOk", "Lkotlin/Function0;", "ensureChannel", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "openSoundPicker", "saveReview", "stars", "", "feeling", "scheduleAlarms", "h1", "m1", "cycles", "hoursText", "todayKey", "app_debug"})
public final class MainActivity extends androidx.activity.ComponentActivity {
    @javax.inject.Inject()
    public com.dormirbien.app.data.local.AlarmPreferences prefs;
    @javax.inject.Inject()
    public com.dormirbien.app.data.repository.SleepRepository sleepRepo;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState<java.lang.Boolean> showReview = null;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit> soundPickedCallback;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> notifLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> exactLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> overlayLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> soundLauncher = null;
    
    public MainActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dormirbien.app.data.local.AlarmPreferences getPrefs() {
        return null;
    }
    
    public final void setPrefs(@org.jetbrains.annotations.NotNull()
    com.dormirbien.app.data.local.AlarmPreferences p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dormirbien.app.data.repository.SleepRepository getSleepRepo() {
        return null;
    }
    
    public final void setSleepRepo(@org.jetbrains.annotations.NotNull()
    com.dormirbien.app.data.repository.SleepRepository p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    private final void scheduleAlarms(int h1, int m1, int cycles, java.lang.String hoursText) {
    }
    
    private final void cancelAlarms() {
    }
    
    private final void cancelBackup() {
    }
    
    private final void saveReview(int stars, java.lang.String feeling) {
    }
    
    private final void openSoundPicker() {
    }
    
    private final void askPermissions() {
    }
    
    private final void dialog(java.lang.String title, java.lang.String msg, kotlin.jvm.functions.Function0<kotlin.Unit> onOk) {
    }
    
    private final void ensureChannel() {
    }
    
    private final java.lang.String todayKey() {
        return null;
    }
}