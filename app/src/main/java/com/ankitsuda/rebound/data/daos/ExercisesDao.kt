package com.ankitsuda.rebound.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ankitsuda.rebound.data.entities.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercisesDao {

    @Query("SELECT * FROM exercises WHERE exercise_id = :exerciseId")
    fun getSingleExercise(exerciseId: Long) : Flow<Exercise>

    @Query("SELECT * FROM exercises ORDER BY name")
    fun getAllExercises() : Flow<List<Exercise>>

    @Insert
    suspend fun insertExercise(exercise: Exercise): Long
}