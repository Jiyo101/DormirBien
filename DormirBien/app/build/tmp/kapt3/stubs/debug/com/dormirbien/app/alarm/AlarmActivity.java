package com.dormirbien.app.alarm;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.activity.ComponentActivity;
import androidx.compose.animation.core.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.unit.*;
import java.text.SimpleDateFormat;
import java.util.*;
import kotlin.math.*;

/**
 * Full-screen activity shown over the lock screen.
 *
 * THE CRITICAL RULES for lock screen display (especially Xiaomi/MIUI):
 *
 * 1. Use ComponentActivity, NOT AppCompatActivity
 *   AppCompatActivity can interfere with window flag timing.
 *
 * 2. Window flags BEFORE super.onCreate() — MANDATORY ORDER:
 *   a. setShowWhenLocked(true) + setTurnScreenOn(true)  [API 27+]
 *   b. window.addFlags(FLAG_SHOW_WHEN_LOCKED | ...)     [all APIs]
 *   c. super.onCreate(savedInstanceState)               ← AFTER flags
 *
 * 3. android:exported="true" in manifest — system must be able to launch it
 *
 * 4. android:showWhenLocked + android:turnScreenOn in manifest — fallback
 *
 * 5. AlarmService calls startActivity() directly after startForeground()
 *   This is the MIUI-compatible path (requires SYSTEM_ALERT_WINDOW)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u0012\u0010\u0007\u001a\u00020\u00062\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0014J\b\u0010\n\u001a\u00020\u0006H\u0014J\b\u0010\u000b\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/dormirbien/app/alarm/AlarmActivity;", "Landroidx/activity/ComponentActivity;", "()V", "dismissReceiver", "Landroid/content/BroadcastReceiver;", "onBackPressed", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "stopAlarm", "app_debug"})
public final class AlarmActivity extends androidx.activity.ComponentActivity {
    @org.jetbrains.annotations.NotNull()
    private final android.content.BroadcastReceiver dismissReceiver = null;
    
    public AlarmActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void stopAlarm() {
    }
    
    @java.lang.Override()
    @kotlin.Suppress(names = {"DEPRECATION"})
    public void onBackPressed() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
}