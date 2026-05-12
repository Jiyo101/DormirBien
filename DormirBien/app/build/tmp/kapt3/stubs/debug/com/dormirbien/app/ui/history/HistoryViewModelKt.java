package com.dormirbien.app.ui.history;

import androidx.lifecycle.ViewModel;
import androidx.compose.foundation.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.unit.*;
import com.dormirbien.app.data.repository.SleepRecord;
import com.dormirbien.app.data.repository.SleepRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000T\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a:\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u000e0\u0016H\u0003\u001a\u0012\u0010\u0018\u001a\u00020\u000e2\b\b\u0002\u0010\u0019\u001a\u00020\u001aH\u0007\u001a\\\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u000e0\u001f2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u000e0\u001f2\u001e\u0010!\u001a\u001a\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u000e0\"H\u0003\u001a\"\u0010#\u001a\u00020\u000e2\u0006\u0010$\u001a\u00020\u00012\u0006\u0010%\u001a\u00020\u0017H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b&\u0010\'\u001a\u001e\u0010(\u001a\u00020\u000e2\u0006\u0010)\u001a\u00020\u00142\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u000e0\u001fH\u0003\u001a2\u0010+\u001a\u00020\u000e2\u0006\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u00172\u0006\u0010$\u001a\u00020\u00012\u0006\u0010.\u001a\u00020/H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b0\u00101\u001a\u0018\u00102\u001a\u00020\u00172\u0006\u00103\u001a\u00020\u00102\u0006\u00104\u001a\u00020\u0010H\u0002\"\u0010\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0003\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0004\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0005\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0006\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0007\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\b\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\t\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\n\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u000b\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\f\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00065"}, d2 = {"ACC", "Landroidx/compose/ui/graphics/Color;", "J", "ACC2", "BG", "CARD", "CARD2", "GRN", "RED", "TXT", "TXT2", "TXT3", "YEL", "CalGrid", "", "year", "", "month", "records", "", "Lcom/dormirbien/app/data/repository/SleepRecord;", "onDayClick", "Lkotlin/Function1;", "", "HistoryRoute", "vm", "Lcom/dormirbien/app/ui/history/HistoryViewModel;", "HistoryScreen", "s", "Lcom/dormirbien/app/ui/history/HistoryUiState$Success;", "onPrev", "Lkotlin/Function0;", "onNext", "onReview", "Lkotlin/Function3;", "LegendDot", "c", "l", "LegendDot-DxMtmZc", "(JLjava/lang/String;)V", "SleepRow", "rec", "onRate", "StatCard", "v", "label", "mod", "Landroidx/compose/ui/Modifier;", "StatCard-9LQNqLg", "(Ljava/lang/String;Ljava/lang/String;JLandroidx/compose/ui/Modifier;)V", "monthName", "m", "y", "app_debug"})
public final class HistoryViewModelKt {
    private static final long BG = 0L;
    private static final long CARD = 0L;
    private static final long CARD2 = 0L;
    private static final long ACC = 0L;
    private static final long ACC2 = 0L;
    private static final long GRN = 0L;
    private static final long YEL = 0L;
    private static final long RED = 0L;
    private static final long TXT = 0L;
    private static final long TXT2 = 0L;
    private static final long TXT3 = 0L;
    
    @androidx.compose.runtime.Composable()
    public static final void HistoryRoute(@org.jetbrains.annotations.NotNull()
    com.dormirbien.app.ui.history.HistoryViewModel vm) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HistoryScreen(com.dormirbien.app.ui.history.HistoryUiState.Success s, int year, int month, kotlin.jvm.functions.Function0<kotlin.Unit> onPrev, kotlin.jvm.functions.Function0<kotlin.Unit> onNext, kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.Integer, ? super java.lang.String, kotlin.Unit> onReview) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CalGrid(int year, int month, java.util.List<com.dormirbien.app.data.repository.SleepRecord> records, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDayClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SleepRow(com.dormirbien.app.data.repository.SleepRecord rec, kotlin.jvm.functions.Function0<kotlin.Unit> onRate) {
    }
    
    private static final java.lang.String monthName(int m, int y) {
        return null;
    }
}