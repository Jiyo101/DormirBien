package com.dormirbien.app.ui.home;

import androidx.lifecycle.ViewModel;
import com.dormirbien.app.data.local.AlarmPreferences;
import com.dormirbien.app.data.local.AlarmState;
import com.dormirbien.app.data.local.UserSettings;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.util.Calendar;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bv\u0018\u00002\u00020\u0001:\u0006\u0002\u0003\u0004\u0005\u0006\u0007\u0082\u0001\u0006\b\t\n\u000b\f\r\u00a8\u0006\u000e"}, d2 = {"Lcom/dormirbien/app/ui/home/HomeAction;", "", "CloseModal", "HourDelta", "MinuteDelta", "OpenModal", "Pick", "SetOnset", "Lcom/dormirbien/app/ui/home/HomeAction$CloseModal;", "Lcom/dormirbien/app/ui/home/HomeAction$HourDelta;", "Lcom/dormirbien/app/ui/home/HomeAction$MinuteDelta;", "Lcom/dormirbien/app/ui/home/HomeAction$OpenModal;", "Lcom/dormirbien/app/ui/home/HomeAction$Pick;", "Lcom/dormirbien/app/ui/home/HomeAction$SetOnset;", "app_debug"})
public abstract interface HomeAction {
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dormirbien/app/ui/home/HomeAction$CloseModal;", "Lcom/dormirbien/app/ui/home/HomeAction;", "()V", "app_debug"})
    public static final class CloseModal implements com.dormirbien.app.ui.home.HomeAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.dormirbien.app.ui.home.HomeAction.CloseModal INSTANCE = null;
        
        private CloseModal() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/dormirbien/app/ui/home/HomeAction$HourDelta;", "Lcom/dormirbien/app/ui/home/HomeAction;", "d", "", "(I)V", "getD", "()I", "component1", "copy", "equals", "", "other", "", "hashCode", "toString", "", "app_debug"})
    public static final class HourDelta implements com.dormirbien.app.ui.home.HomeAction {
        private final int d = 0;
        
        public HourDelta(int d) {
            super();
        }
        
        public final int getD() {
            return 0;
        }
        
        public final int component1() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.dormirbien.app.ui.home.HomeAction.HourDelta copy(int d) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/dormirbien/app/ui/home/HomeAction$MinuteDelta;", "Lcom/dormirbien/app/ui/home/HomeAction;", "d", "", "(I)V", "getD", "()I", "component1", "copy", "equals", "", "other", "", "hashCode", "toString", "", "app_debug"})
    public static final class MinuteDelta implements com.dormirbien.app.ui.home.HomeAction {
        private final int d = 0;
        
        public MinuteDelta(int d) {
            super();
        }
        
        public final int getD() {
            return 0;
        }
        
        public final int component1() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.dormirbien.app.ui.home.HomeAction.MinuteDelta copy(int d) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dormirbien/app/ui/home/HomeAction$OpenModal;", "Lcom/dormirbien/app/ui/home/HomeAction;", "()V", "app_debug"})
    public static final class OpenModal implements com.dormirbien.app.ui.home.HomeAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.dormirbien.app.ui.home.HomeAction.OpenModal INSTANCE = null;
        
        private OpenModal() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/dormirbien/app/ui/home/HomeAction$Pick;", "Lcom/dormirbien/app/ui/home/HomeAction;", "o", "Lcom/dormirbien/app/ui/home/AlarmOption;", "(Lcom/dormirbien/app/ui/home/AlarmOption;)V", "getO", "()Lcom/dormirbien/app/ui/home/AlarmOption;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Pick implements com.dormirbien.app.ui.home.HomeAction {
        @org.jetbrains.annotations.NotNull()
        private final com.dormirbien.app.ui.home.AlarmOption o = null;
        
        public Pick(@org.jetbrains.annotations.NotNull()
        com.dormirbien.app.ui.home.AlarmOption o) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.dormirbien.app.ui.home.AlarmOption getO() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.dormirbien.app.ui.home.AlarmOption component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.dormirbien.app.ui.home.HomeAction.Pick copy(@org.jetbrains.annotations.NotNull()
        com.dormirbien.app.ui.home.AlarmOption o) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/dormirbien/app/ui/home/HomeAction$SetOnset;", "Lcom/dormirbien/app/ui/home/HomeAction;", "v", "", "(I)V", "getV", "()I", "component1", "copy", "equals", "", "other", "", "hashCode", "toString", "", "app_debug"})
    public static final class SetOnset implements com.dormirbien.app.ui.home.HomeAction {
        private final int v = 0;
        
        public SetOnset(int v) {
            super();
        }
        
        public final int getV() {
            return 0;
        }
        
        public final int component1() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.dormirbien.app.ui.home.HomeAction.SetOnset copy(int v) {
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