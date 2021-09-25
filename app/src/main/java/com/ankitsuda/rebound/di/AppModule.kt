package com.ankitsuda.rebound.di

import android.content.Context
import com.ankitsuda.rebound.AppDatabase
import com.ankitsuda.rebound.data.datastore.AppPreferences
import com.ankitsuda.rebound.data.datastore.PrefStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun providesPrefStorage(
        appPreferences: AppPreferences
    ): PrefStorage

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideMeasurementsDao(db: AppDatabase) = db.measurementsDao()

}