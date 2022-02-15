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

import com.ankitsuda.base.utils.generateId
import com.ankitsuda.rebound.data.db.daos.ExercisesDao
import com.ankitsuda.rebound.domain.ExerciseCategory
import com.ankitsuda.rebound.domain.entities.Exercise
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class ExercisesRepository @Inject constructor(private val exercisesDao: ExercisesDao) {

    fun getExercise(exerciseId: String) = exercisesDao.getSingleExercise(exerciseId)

    fun getAllLogEntries(exerciseId: String) = exercisesDao.getAllLogEntries(exerciseId)

    fun getAllExercises() = exercisesDao.getAllExercises()
    fun getAllExercisesWithMuscles() = exercisesDao.getAllExercisesWithMuscles()

    suspend fun createExercise(
        name: String? = null,
        notes: String? = null,
        primaryMuscleTag: String? = null,
        secondaryMuscleTag: String? = null,
        category: ExerciseCategory = ExerciseCategory.UNKNOWN,
    ) {
        exercisesDao.insertExercise(
            Exercise(
                exerciseId = generateId(),
                name = name,
                notes = notes,
                primaryMuscleTag = primaryMuscleTag,
                secondaryMuscleTag = secondaryMuscleTag,
                category = category,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
    }

}