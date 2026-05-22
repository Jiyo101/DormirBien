package com.dormirbien.app.di

import android.content.Context
import androidx.room.Room
import com.dormirbien.app.data.local.*
import com.dormirbien.app.data.repository.*
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

    @Provides @Singleton
    fun provideUsuarioDao(db: SleepDatabase): UsuarioDao = db.usuarioDao()

    @Provides @Singleton
    fun provideAjustesDao(db: SleepDatabase): AjustesDao = db.ajustesDao()
}

@Module @InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindSleepRepo(impl: OfflineFirstSleepRepository): SleepRepository

    @Binds @Singleton
    abstract fun bindUsuarioRepo(impl: OfflineFirstUsuarioRepository): UsuarioRepository

    @Binds @Singleton
    abstract fun bindAjustesRepo(impl: OfflineFirstAjustesRepository): AjustesRepository
}
