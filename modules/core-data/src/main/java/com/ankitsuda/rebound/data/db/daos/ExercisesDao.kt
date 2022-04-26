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

package com.ankitsuda.rebound.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.ankitsuda.rebound.domain.entities.Exercise
import com.ankitsuda.rebound.domain.entities.ExerciseWithMuscle
import com.ankitsuda.rebound.domain.entities.LogEntriesWithWorkout
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercisesDao {

    @Query("SELECT * FROM exercises WHERE exercise_id = :exerciseId")
    fun getSingleExercise(exerciseId: String): Flow<Exercise>

    @Query("SELECT * FROM exercises ORDER BY name")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises ORDER BY name")
    fun getAllExercisesWithMuscles(): Flow<List<ExerciseWithMuscle>>

    @Transaction
    @Query("SELECT * FROM exercise_workout_junctions WHERE exercise_id = :exerciseId")
    fun getAllLogEntries(exerciseId: String): Flow<List<LogEntriesWithWorkout>>

    @Transaction
    @Query("SELECT exercise_workout_junctions.* FROM exercise_workout_junctions JOIN workouts WHERE exercise_id = :exerciseId AND workouts.id = workout_id AND workouts.is_hidden = 0 AND workouts.in_progress = 0 ORDER BY start_at")
    fun getVisibleLogEntries(exerciseId: String): Flow<List<LogEntriesWithWorkout>>

    @Transaction
    @Query("SELECT COUNT(exercise_workout_junctions.id) FROM exercise_workout_junctions JOIN workouts WHERE exercise_id = :exerciseId AND workouts.id = workout_id AND workouts.is_hidden = 0 AND workouts.in_progress = 0 ORDER BY start_at")
    fun getVisibleLogEntriesCount(exerciseId: String): Flow<Long>

    @Insert
    suspend fun insertExercise(exercise: Exercise)
}