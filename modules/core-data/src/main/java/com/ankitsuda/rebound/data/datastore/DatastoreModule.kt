package com.ankitsuda.rebound.data.datastore

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class DatastoreModule {
    // interface with @Binds
    @Binds
    abstract fun providesPrefStorage(
        appPreferences: AppPreferences
    ): PrefStorage
}