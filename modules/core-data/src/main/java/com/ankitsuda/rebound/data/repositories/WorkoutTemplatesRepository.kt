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

import androidx.room.Transaction
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.rebound.data.db.daos.WorkoutTemplatesDao
import com.ankitsuda.rebound.data.db.daos.WorkoutsDao
import com.ankitsuda.rebound.domain.entities.WorkoutTemplate
import com.ankitsuda.rebound.domain.entities.Workout
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class WorkoutTemplatesRepository @Inject constructor(
    private val workoutTemplatesDao: WorkoutTemplatesDao,
    private val workoutsDao: WorkoutsDao
) {

    fun getNonHiddenTemplatesWithWorkouts() =
        workoutTemplatesDao.getNonHiddenTemplatesWithWorkouts()

    @Transaction
    suspend fun createTemplate(): String {
        val workoutId = generateId()
        workoutsDao.insertWorkout(
            Workout(
                id = workoutId,
                isHidden = true,
                inProgress = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        )

        val routineId = generateId()
        val lastListOrder = workoutTemplatesDao.getLastListOrder().firstOrNull() ?: 0

        workoutTemplatesDao.insertTemplate(
            WorkoutTemplate(
                id = routineId,
                workoutId = workoutId,
                isArchived = false,
                isHidden = false,
                listOrder = lastListOrder + 1,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        return routineId
    }
}