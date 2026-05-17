package com.dormirbien.app.di

import android.content.Context
import androidx.room.Room
import com.dormirbien.app.data.local.SleepDao
import com.dormirbien.app.data.local.SleepDatabase
import com.dormirbien.app.data.repository.OfflineFirstSleepRepository
import com.dormirbien.app.data.repository.SleepRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): SleepDatabase =
        Room.databaseBuilder(ctx, SleepDatabase::class.java, SleepDatabase.NAME)
            .fallbackToDestructiveMigration().build()

    @Provides @Singleton
    fun provideDao(db: SleepDatabase): SleepDao = db.sleepDao()
}

@Module @InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindSleepRepo(impl: OfflineFirstSleepRepository): SleepRepository
}
