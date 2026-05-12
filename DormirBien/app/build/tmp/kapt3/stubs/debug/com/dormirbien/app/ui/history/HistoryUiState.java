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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bv\u0018\u00002\u00020\u0001:\u0002\u0002\u0003\u0082\u0001\u0002\u0004\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/dormirbien/app/ui/history/HistoryUiState;", "", "Loading", "Success", "Lcom/dormirbien/app/ui/history/HistoryUiState$Loading;", "Lcom/dormirbien/app/ui/history/HistoryUiState$Success;", "app_debug"})
public abstract interface HistoryUiState {
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dormirbien/app/ui/history/HistoryUiState$Loading;", "Lcom/dormirbien/app/ui/history/HistoryUiState;", "()V", "app_debug"})
    public static final class Loading implements com.dormirbien.app.ui.history.HistoryUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.dormirbien.app.ui.history.HistoryUiState.Loading INSTANCE = null;
        
        private Loading() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0015\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B;\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u0012\u0006\u0010\n\u001a\u00020\b\u0012\u0006\u0010\u000b\u001a\u00020\b\u00a2\u0006\u0002\u0010\fJ\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\bH\u00c6\u0003J\t\u0010\u0019\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001a\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\bH\u00c6\u0003JK\u0010\u001c\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\b2\b\b\u0002\u0010\u000b\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u00d6\u0003J\t\u0010!\u001a\u00020\bH\u00d6\u0001J\t\u0010\"\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u000b\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010R\u0011\u0010\n\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010\u00a8\u0006#"}, d2 = {"Lcom/dormirbien/app/ui/history/HistoryUiState$Success;", "Lcom/dormirbien/app/ui/history/HistoryUiState;", "records", "", "Lcom/dormirbien/app/data/repository/SleepRecord;", "avg", "", "goodDays", "", "streak", "year", "month", "(Ljava/util/List;Ljava/lang/String;IIII)V", "getAvg", "()Ljava/lang/String;", "getGoodDays", "()I", "getMonth", "getRecords", "()Ljava/util/List;", "getStreak", "getYear", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "", "hashCode", "toString", "app_debug"})
    public static final class Success implements com.dormirbien.app.ui.history.HistoryUiState {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.dormirbien.app.data.repository.SleepRecord> records = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String avg = null;
        private final int goodDays = 0;
        private final int streak = 0;
        private final int year = 0;
        private final int month = 0;
        
        public Success(@org.jetbrains.annotations.NotNull()
        java.util.List<com.dormirbien.app.data.repository.SleepRecord> records, @org.jetbrains.annotations.NotNull()
        java.lang.String avg, int goodDays, int streak, int year, int month) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.dormirbien.app.data.repository.SleepRecord> getRecords() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getAvg() {
            return null;
        }
        
        public final int getGoodDays() {
            return 0;
        }
        
        public final int getStreak() {
            return 0;
        }
        
        public final int getYear() {
            return 0;
        }
        
        public final int getMonth() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.dormirbien.app.data.repository.SleepRecord> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        public final int component3() {
            return 0;
        }
        
        public final int component4() {
            return 0;
        }
        
        public final int component5() {
            return 0;
        }
        
        public final int component6() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.dormirbien.app.ui.history.HistoryUiState.Success copy(@org.jetbrains.annotations.NotNull()
        java.util.List<com.dormirbien.app.data.repository.SleepRecord> records, @org.jetbrains.annotations.NotNull()
        java.lang.String avg, int goodDays, int streak, int year, int month) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}