package com.dormirbien.app.data.repository;

import com.dormirbien.app.data.local.SleepDao;
import com.dormirbien.app.data.local.SleepRecordEntity;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\f0\u000bH\u0016J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\u0010J\f\u0010\u0011\u001a\u00020\u0006*\u00020\u0012H\u0002J\f\u0010\u0013\u001a\u00020\u0012*\u00020\u0006H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/dormirbien/app/data/repository/OfflineFirstSleepRepository;", "Lcom/dormirbien/app/data/repository/SleepRepository;", "dao", "Lcom/dormirbien/app/data/local/SleepDao;", "(Lcom/dormirbien/app/data/local/SleepDao;)V", "getByKey", "Lcom/dormirbien/app/data/repository/SleepRecord;", "key", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeAll", "Lkotlinx/coroutines/flow/Flow;", "", "upsert", "", "record", "(Lcom/dormirbien/app/data/repository/SleepRecord;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toDomain", "Lcom/dormirbien/app/data/local/SleepRecordEntity;", "toEntity", "app_debug"})
public final class OfflineFirstSleepRepository implements com.dormirbien.app.data.repository.SleepRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dormirbien.app.data.local.SleepDao dao = null;
    
    @javax.inject.Inject()
    public OfflineFirstSleepRepository(@org.jetbrains.annotations.NotNull()
    com.dormirbien.app.data.local.SleepDao dao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dormirbien.app.data.repository.SleepRecord>> observeAll() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getByKey(@org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dormirbien.app.data.repository.SleepRecord> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.dormirbien.app.data.repository.SleepRecord record, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.dormirbien.app.data.repository.SleepRecord toDomain(com.dormirbien.app.data.local.SleepRecordEntity $this$toDomain) {
        return null;
    }
    
    private final com.dormirbien.app.data.local.SleepRecordEntity toEntity(com.dormirbien.app.data.repository.SleepRecord $this$toEntity) {
        return null;
    }
}