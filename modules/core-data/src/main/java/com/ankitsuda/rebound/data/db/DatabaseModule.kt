package com.ankitsuda.rebound.data.db

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideMeasurementsDao(db: AppDatabase) = db.measurementsDao()

    @Singleton
    @Provides
    fun provideWorkoutsDao(db: AppDatabase) = db.workoutsDao()

    @Singleton
    @Provides
    fun provideMusclesDao(db: AppDatabase) = db.musclesDao()

    @Singleton
    @Provides
    fun provideExercisesDao(db: AppDatabase) = db.exercisesDao()

}
