package com.ankitsuda.rebound.di

import com.ankitsuda.rebound.data.datastore.AppPreferences
import com.ankitsuda.rebound.data.datastore.PrefStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun providesPrefStorage(
        appPreferences: AppPreferences
    ): PrefStorage

}