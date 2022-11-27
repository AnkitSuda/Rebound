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

package com.ankitsuda.rebound.data.repositories

import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.db.daos.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoriesModule {

    @Singleton
    @Provides
    fun provideMeasurementsRepository(measurementsDao: MeasurementsDao) =
        MeasurementsRepository(measurementsDao)

    @Singleton
    @Provides
    fun provideWorkoutsRepository(
        workoutsDao: WorkoutsDao,
        templatesDao: WorkoutTemplatesDao,
        prefStorage: PrefStorage
    ) =
        WorkoutsRepository(workoutsDao, templatesDao, prefStorage)

    @Singleton
    @Provides
    fun provideMusclesRepository(musclesDao: MusclesDao) =
        MusclesRepository(musclesDao)

    @Singleton
    @Provides
    fun provideExercisesRepository(
        exercisesDao: ExercisesDao,
        musclesDao: MusclesDao
    ) =
        ExercisesRepository(exercisesDao, musclesDao)

    @Singleton
    @Provides
    fun provideWorkoutTemplatesRepository(
        workoutTemplatesDao: WorkoutTemplatesDao,
        foldersDao: WorkoutFoldersFoldersDao,
        workoutsDao: WorkoutsDao,
    ) =
        WorkoutTemplatesRepository(workoutTemplatesDao, foldersDao, workoutsDao)

    @Singleton
    @Provides
    fun provideThemePresetsRepository(
        presetsDao: ThemePresetsDao
    ) =
        ThemePresetsRepository(presetsDao)

    @Singleton
    @Provides
    fun provideBarbellsRepository(
        barbellsDao: BarbellsDao
    ) =
        BarbellsRepository(barbellsDao)
}