package com.dormirbien.app.ui;

import android.net.Uri;
import androidx.compose.animation.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.navigation.compose.*;
import com.dormirbien.app.data.local.AlarmPreferences;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000X\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u00a2\u0001\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00112\u0018\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u000b0\u00132$\u0010\u0016\u001a \u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u000b0\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00112\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00112\u001e\u0010\u001a\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u000b0\u001b\u0012\u0004\u0012\u00020\u000b0\u001bH\u0007\"\u0010\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"BG", "Landroidx/compose/ui/graphics/Color;", "J", "DESTS", "", "Lcom/dormirbien/app/ui/Dest;", "ENTER", "Landroidx/compose/animation/EnterTransition;", "EXIT", "Landroidx/compose/animation/ExitTransition;", "AppRoot", "", "prefs", "Lcom/dormirbien/app/data/local/AlarmPreferences;", "showReview", "", "onReviewDismiss", "Lkotlin/Function0;", "onReviewSave", "Lkotlin/Function2;", "", "", "onScheduleAlarms", "Lkotlin/Function4;", "onCancelAlarms", "onCancelBackup", "onOpenSoundPicker", "Lkotlin/Function1;", "Landroid/net/Uri;", "app_debug"})
public final class AppRootKt {
    private static final long BG = 0L;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.dormirbien.app.ui.Dest> DESTS = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.animation.EnterTransition ENTER = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.animation.ExitTransition EXIT = null;
    
    @androidx.compose.runtime.Composable()
    public static final void AppRoot(@org.jetbrains.annotations.NotNull()
    com.dormirbien.app.data.local.AlarmPreferences prefs, boolean showReview, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onReviewDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.String, kotlin.Unit> onReviewSave, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function4<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.String, kotlin.Unit> onScheduleAlarms, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCancelAlarms, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCancelBackup, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit>, kotlin.Unit> onOpenSoundPicker) {
    }
}