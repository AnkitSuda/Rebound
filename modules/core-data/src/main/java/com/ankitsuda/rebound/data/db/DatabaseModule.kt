/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

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

    @Singleton
    @Provides
    fun provideWorkoutTemplatesDao(db: AppDatabase) = db.workoutTemplatesDao()

    @Singleton
    @Provides
    fun providePlatesDao(db: AppDatabase) = db.platesDao()

    @Singleton
    @Provides
    fun provideThemePresetsDao(db: AppDatabase) = db.themePresetsDao()

    @Provides
    fun provideWorkoutFoldersFoldersDao(db: AppDatabase) = db.workoutFoldersFoldersDao()

}
