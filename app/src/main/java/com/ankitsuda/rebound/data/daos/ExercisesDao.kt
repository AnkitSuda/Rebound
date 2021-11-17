package com.ankitsuda.rebound.data.daos

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
    fun getSingleExercise(exerciseId: Long): Flow<Exercise>

    @Query("SELECT * FROM exercises ORDER BY name")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises ORDER BY name")
    fun getAllExercisesWithMuscles(): Flow<List<ExerciseWithMuscle>>

    @Transaction
    @Query("SELECT * FROM exercise_workout_junctions WHERE exercise_id = :exerciseId")
    fun getAllLogEntries(exerciseId: Long): Flow<List<LogEntriesWithWorkout>>

    @Insert
    suspend fun insertExercise(exercise: Exercise): Long
}