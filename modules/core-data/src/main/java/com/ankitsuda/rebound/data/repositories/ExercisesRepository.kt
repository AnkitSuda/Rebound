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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.rebound.data.db.daos.ExercisesDao
import com.ankitsuda.rebound.data.db.daos.MusclesDao
import com.ankitsuda.rebound.domain.ExerciseCategory
import com.ankitsuda.rebound.domain.entities.Exercise
import com.ankitsuda.rebound.domain.entities.ExerciseWithExtraInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class ExercisesRepository @Inject constructor(
    private val exercisesDao: ExercisesDao,
    private val musclesDao: MusclesDao,
) {

    fun getExercise(exerciseId: String) = exercisesDao.getSingleExercise(exerciseId)

    fun getAllLogEntries(exerciseId: String) = exercisesDao.getAllLogEntries(exerciseId)
    fun getVisibleLogEntries(exerciseId: String) = exercisesDao.getVisibleLogEntries(exerciseId)
    fun getVisibleLogEntriesCount(exerciseId: String) =
        exercisesDao.getVisibleLogEntriesCount(exerciseId)

    fun getAllExercises() = exercisesDao.getAllExercises()
    fun getAllExercisesWithMuscles() =
        exercisesDao.getAllExercisesWithMuscles()

    fun getExercisesWithExtraInfo() = exercisesDao.getAllExercises().map {
        val list = arrayListOf<ExerciseWithExtraInfo>()

        for (exercise in it) {
            val logsCount = getVisibleLogEntriesCount(exercise.exerciseId).first()
            val primaryMuscle =
                if (exercise.primaryMuscleTag != null) musclesDao.getMuscle(exercise.primaryMuscleTag!!)
                    .firstOrNull() else null
            list.add(
                ExerciseWithExtraInfo(
                    exercise = exercise,
                    primaryMuscle = primaryMuscle,
                    logsCount = logsCount
                )
            )
        }

        list
    }

    fun getExercisesWithExtraInfoPaged(searchQuery: String? = null) =
        Pager(PagingConfig(pageSize = 15)) {
//            if (searchQuery != null) {
                exercisesDao.getAllExercisesWithExtraInfoPaged(searchQuery = searchQuery)
//            } else {
//                exercisesDao.getAllExercisesWithExtraInfoPaged()
//            }
        }.flow


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