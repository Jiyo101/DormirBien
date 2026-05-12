package com.dormirbien.app.ui.home;

import android.net.Uri;
import androidx.compose.animation.core.*;
import androidx.compose.foundation.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.shape.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.*;
import androidx.compose.ui.graphics.drawscope.DrawScope;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.unit.*;
import com.dormirbien.app.alarm.AlarmScheduler;
import com.dormirbien.app.data.local.AlarmPreferences;
import com.dormirbien.app.data.local.AlarmState;
import kotlin.math.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u008c\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\u001ad\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\b\u0010\u0012\u001a\u0004\u0018\u00010\u00112\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u000e0\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00162\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u001a0\u0014H\u0003\u001a,\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u001d2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00162\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0016H\u0003\u001a0\u0010 \u001a\u00020\u000e2\b\b\u0002\u0010!\u001a\u00020\"2\u001c\u0010#\u001a\u0018\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020\u000e0\u0014\u00a2\u0006\u0002\b%\u00a2\u0006\u0002\b&H\u0003\u001a\u008c\u0001\u0010\'\u001a\u00020\u000e2\u0006\u0010(\u001a\u00020)2$\u0010*\u001a \u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u000e0+2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00162\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00162\u001e\u0010.\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020\u000e0\u0014\u0012\u0004\u0012\u00020\u000e0\u00142\u000e\b\u0002\u00100\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00162\b\b\u0002\u00101\u001a\u000202H\u0007\u001a|\u00103\u001a\u00020\u000e2\u0006\u00104\u001a\u0002052\u0012\u00106\u001a\u000e\u0012\u0004\u0012\u000207\u0012\u0004\u0012\u00020\u000e0\u00142\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00162\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00162\u001e\u0010.\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020\u000e0\u0014\u0012\u0004\u0012\u00020\u000e0\u00142\f\u00108\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00162\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0016H\u0003\u001a\u0018\u00109\u001a\u00020\u000e2\u0006\u0010:\u001a\u00020\u00192\u0006\u0010;\u001a\u00020\u0019H\u0003\u001a\b\u0010<\u001a\u00020\u000eH\u0003\u001a\u0016\u0010=\u001a\u00020\u000e2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0016H\u0003\u001a\u0016\u0010?\u001a\u00020\u000e2\f\u0010@\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0016H\u0003\u001a$\u0010A\u001a\u00020\u000e2\u0006\u0010B\u001a\u00020\u00192\u0012\u0010C\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u000e0\u0014H\u0003\u001aD\u0010D\u001a\u00020\u000e2\u0006\u0010E\u001a\u00020\u00192\u0012\u0010F\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u000e0\u00142\u001e\u0010G\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020\u000e0\u0014\u0012\u0004\u0012\u00020\u000e0\u0014H\u0003\u001a(\u0010H\u001a\u00020\u000e2\u001e\u0010\u0013\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020\u000e0\u0014\u0012\u0004\u0012\u00020\u000e0\u0014H\u0003\u001a,\u0010I\u001a\u00020\u000e2\u0006\u0010B\u001a\u00020\u00192\f\u0010J\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00162\f\u0010K\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0016H\u0003\u001a@\u0010L\u001a\u00020\u000e2\u0006\u0010:\u001a\u00020\u00192\u0006\u0010;\u001a\u00020\u00192\u0012\u0010M\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u000e0\u00142\u0012\u0010N\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u000e0\u0014H\u0003\u001a\u0014\u0010O\u001a\u00020\u000e*\u00020P2\u0006\u0010Q\u001a\u00020RH\u0002\"\u0010\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0003\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0004\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0005\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0006\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0007\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\b\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\t\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\n\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u000b\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\f\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\u00a8\u0006S"}, d2 = {"ACC", "Landroidx/compose/ui/graphics/Color;", "J", "ACC2", "BDR", "BG", "CARD", "CARD2", "GRN", "RED", "TXT", "TXT2", "TXT3", "AlarmModal", "", "options", "", "Lcom/dormirbien/app/ui/home/AlarmOption;", "selected", "onPick", "Lkotlin/Function1;", "onConfirm", "Lkotlin/Function0;", "onDismiss", "fmt", "", "", "AlarmStatusCard", "a", "Lcom/dormirbien/app/data/local/AlarmState;", "onCancel", "onCancelBkp", "DbCard", "modifier", "Landroidx/compose/ui/Modifier;", "content", "Landroidx/compose/foundation/layout/ColumnScope;", "Landroidx/compose/runtime/Composable;", "Lkotlin/ExtensionFunctionType;", "HomeRoute", "prefs", "Lcom/dormirbien/app/data/local/AlarmPreferences;", "onScheduleAlarms", "Lkotlin/Function4;", "onCancelAlarms", "onCancelBackup", "onOpenSoundPicker", "Landroid/net/Uri;", "onShowMiuiGuide", "vm", "Lcom/dormirbien/app/ui/home/HomeViewModel;", "HomeScreen", "s", "Lcom/dormirbien/app/ui/home/HomeUiState$Success;", "onAction", "Lcom/dormirbien/app/ui/home/HomeAction;", "onOpenModal", "LiveClock", "h", "m", "LogoRow", "MiuiGuideCard", "onShowGuide", "MoonButton", "onClick", "OnsetSlider", "v", "onChange", "SettingsRow", "onset", "onOnset", "onPickSound", "SoundRow", "TimeCol", "up", "dn", "WakePicker", "onHour", "onMin", "drawLogo", "Landroidx/compose/ui/graphics/drawscope/DrawScope;", "S", "", "app_debug"})
public final class HomeScreenKt {
    private static final long BG = 0L;
    private static final long CARD = 0L;
    private static final long CARD2 = 0L;
    private static final long ACC = 0L;
    private static final long ACC2 = 0L;
    private static final long GRN = 0L;
    private static final long RED = 0L;
    private static final long TXT = 0L;
    private static final long TXT2 = 0L;
    private static final long TXT3 = 0L;
    private static final long BDR = 0L;
    
    @androidx.compose.runtime.Composable()
    public static final void HomeRoute(@org.jetbrains.annotations.NotNull()
    com.dormirbien.app.data.local.AlarmPreferences prefs, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function4<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.String, kotlin.Unit> onScheduleAlarms, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCancelAlarms, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCancelBackup, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit>, kotlin.Unit> onOpenSoundPicker, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onShowMiuiGuide, @org.jetbrains.annotations.NotNull()
    com.dormirbien.app.ui.home.HomeViewModel vm) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HomeScreen(com.dormirbien.app.ui.home.HomeUiState.Success s, kotlin.jvm.functions.Function1<? super com.dormirbien.app.ui.home.HomeAction, kotlin.Unit> onAction, kotlin.jvm.functions.Function0<kotlin.Unit> onCancelAlarms, kotlin.jvm.functions.Function0<kotlin.Unit> onCancelBackup, kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit>, kotlin.Unit> onOpenSoundPicker, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenModal, kotlin.jvm.functions.Function0<kotlin.Unit> onShowMiuiGuide) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LogoRow() {
    }
    
    private static final void drawLogo(androidx.compose.ui.graphics.drawscope.DrawScope $this$drawLogo, float S) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LiveClock(int h, int m) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void WakePicker(int h, int m, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onHour, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onMin) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TimeCol(int v, kotlin.jvm.functions.Function0<kotlin.Unit> up, kotlin.jvm.functions.Function0<kotlin.Unit> dn) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OnsetSlider(int v, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SoundRow(kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit>, kotlin.Unit> onPick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SettingsRow(int onset, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onOnset, kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit>, kotlin.Unit> onPickSound) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AlarmStatusCard(com.dormirbien.app.data.local.AlarmState a, kotlin.jvm.functions.Function0<kotlin.Unit> onCancel, kotlin.jvm.functions.Function0<kotlin.Unit> onCancelBkp) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MiuiGuideCard(kotlin.jvm.functions.Function0<kotlin.Unit> onShowGuide) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MoonButton(kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void AlarmModal(java.util.List<com.dormirbien.app.ui.home.AlarmOption> options, com.dormirbien.app.ui.home.AlarmOption selected, kotlin.jvm.functions.Function1<? super com.dormirbien.app.ui.home.AlarmOption, kotlin.Unit> onPick, kotlin.jvm.functions.Function0<kotlin.Unit> onConfirm, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function1<? super java.lang.Integer, java.lang.String> fmt) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DbCard(androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function1<? super androidx.compose.foundation.layout.ColumnScope, kotlin.Unit> content) {
    }
}