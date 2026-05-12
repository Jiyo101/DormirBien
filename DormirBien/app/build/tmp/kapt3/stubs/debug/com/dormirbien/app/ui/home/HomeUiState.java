package com.dormirbien.app.ui.home;

import androidx.lifecycle.ViewModel;
import com.dormirbien.app.data.local.AlarmPreferences;
import com.dormirbien.app.data.local.AlarmState;
import com.dormirbien.app.data.local.UserSettings;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.util.Calendar;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bv\u0018\u00002\u00020\u0001:\u0002\u0002\u0003\u0082\u0001\u0002\u0004\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/dormirbien/app/ui/home/HomeUiState;", "", "Loading", "Success", "Lcom/dormirbien/app/ui/home/HomeUiState$Loading;", "Lcom/dormirbien/app/ui/home/HomeUiState$Success;", "app_debug"})
public abstract interface HomeUiState {
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/dormirbien/app/ui/home/HomeUiState$Loading;", "Lcom/dormirbien/app/ui/home/HomeUiState;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Loading implements com.dormirbien.app.ui.home.HomeUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.dormirbien.app.ui.home.HomeUiState.Loading INSTANCE = null;
        
        private Loading() {
            super();
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u001b\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BU\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\t\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u0012\b\u0010\r\u001a\u0004\u0018\u00010\f\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0010J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\tH\u00c6\u0003J\u000f\u0010%\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u00c6\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\t\u0010\'\u001a\u00020\u000fH\u00c6\u0003Jk\u0010(\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u00c6\u0001J\u0013\u0010)\u001a\u00020\u000f2\b\u0010*\u001a\u0004\u0018\u00010+H\u00d6\u0003J\t\u0010,\u001a\u00020\u0003H\u00d6\u0001J\t\u0010-\u001a\u00020.H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\r\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0014R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0014\u00a8\u0006/"}, d2 = {"Lcom/dormirbien/app/ui/home/HomeUiState$Success;", "Lcom/dormirbien/app/ui/home/HomeUiState;", "nowH", "", "nowM", "wakeH", "wakeM", "onset", "alarm", "Lcom/dormirbien/app/data/local/AlarmState;", "options", "", "Lcom/dormirbien/app/ui/home/AlarmOption;", "selected", "showModal", "", "(IIIIILcom/dormirbien/app/data/local/AlarmState;Ljava/util/List;Lcom/dormirbien/app/ui/home/AlarmOption;Z)V", "getAlarm", "()Lcom/dormirbien/app/data/local/AlarmState;", "getNowH", "()I", "getNowM", "getOnset", "getOptions", "()Ljava/util/List;", "getSelected", "()Lcom/dormirbien/app/ui/home/AlarmOption;", "getShowModal", "()Z", "getWakeH", "getWakeM", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "", "hashCode", "toString", "", "app_debug"})
    public static final class Success implements com.dormirbien.app.ui.home.HomeUiState {
        private final int nowH = 0;
        private final int nowM = 0;
        private final int wakeH = 0;
        private final int wakeM = 0;
        private final int onset = 0;
        @org.jetbrains.annotations.NotNull()
        private final com.dormirbien.app.data.local.AlarmState alarm = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.dormirbien.app.ui.home.AlarmOption> options = null;
        @org.jetbrains.annotations.Nullable()
        private final com.dormirbien.app.ui.home.AlarmOption selected = null;
        private final boolean showModal = false;
        
        public Success(int nowH, int nowM, int wakeH, int wakeM, int onset, @org.jetbrains.annotations.NotNull()
        com.dormirbien.app.data.local.AlarmState alarm, @org.jetbrains.annotations.NotNull()
        java.util.List<com.dormirbien.app.ui.home.AlarmOption> options, @org.jetbrains.annotations.Nullable()
        com.dormirbien.app.ui.home.AlarmOption selected, boolean showModal) {
            super();
        }
        
        public final int getNowH() {
            return 0;
        }
        
        public final int getNowM() {
            return 0;
        }
        
        public final int getWakeH() {
            return 0;
        }
        
        public final int getWakeM() {
            return 0;
        }
        
        public final int getOnset() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.dormirbien.app.data.local.AlarmState getAlarm() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.dormirbien.app.ui.home.AlarmOption> getOptions() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.dormirbien.app.ui.home.AlarmOption getSelected() {
            return null;
        }
        
        public final boolean getShowModal() {
            return false;
        }
        
        public final int component1() {
            return 0;
        }
        
        public final int component2() {
            return 0;
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
        
        @org.jetbrains.annotations.NotNull()
        public final com.dormirbien.app.data.local.AlarmState component6() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.dormirbien.app.ui.home.AlarmOption> component7() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.dormirbien.app.ui.home.AlarmOption component8() {
            return null;
        }
        
        public final boolean component9() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.dormirbien.app.ui.home.HomeUiState.Success copy(int nowH, int nowM, int wakeH, int wakeM, int onset, @org.jetbrains.annotations.NotNull()
        com.dormirbien.app.data.local.AlarmState alarm, @org.jetbrains.annotations.NotNull()
        java.util.List<com.dormirbien.app.ui.home.AlarmOption> options, @org.jetbrains.annotations.Nullable()
        com.dormirbien.app.ui.home.AlarmOption selected, boolean showModal) {
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