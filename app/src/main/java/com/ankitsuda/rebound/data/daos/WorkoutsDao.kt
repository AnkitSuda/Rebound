package com.ankitsuda.rebound.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ankitsuda.rebound.data.entities.ExerciseWorkoutJunction
import com.ankitsuda.rebound.data.entities.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutsDao {

    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    fun getWorkout(workoutId: Long): Flow<Workout?>

    @Query("SELECT * FROM exercise_workout_junctions WHERE workout_id = :workoutId")
    fun getExerciseWorkoutJunction(workoutId: Long): Flow<List<ExerciseWorkoutJunction>>

    @Insert
    suspend fun insertWorkout(workout: Workout): Long

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Insert
    suspend fun insertExerciseWorkoutJunction(exerciseWorkoutJunction: ExerciseWorkoutJunction): Long
}