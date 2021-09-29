package com.ankitsuda.rebound.data.daos

import androidx.room.*
import com.ankitsuda.rebound.data.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutsDao {

    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    fun getWorkout(workoutId: Long): Flow<Workout?>

    @Query("SELECT * FROM exercise_workout_junctions WHERE workout_id = :workoutId")
    fun getExerciseWorkoutJunction(workoutId: Long): Flow<List<ExerciseWorkoutJunction>>

    @Transaction
    @Query("SELECT * FROM exercise_workout_junctions WHERE workout_id = :workoutId")
    fun getLogEntriesWithExerciseJunction(workoutId: Long): Flow<List<LogEntriesWithExerciseJunction>>

    @Delete
    suspend fun deleteExerciseLogEntry(exerciseLogEntry: ExerciseLogEntry)

    @Update
    suspend fun updateExerciseLogEntry(exerciseLogEntry: ExerciseLogEntry)

    @Insert
    suspend fun insertWorkout(workout: Workout): Long

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Insert
    suspend fun insertExerciseWorkoutJunction(exerciseWorkoutJunction: ExerciseWorkoutJunction): Long

    @Insert
    suspend fun insertExerciseLog(log: ExerciseLog): Long

    @Insert
    suspend fun insertExerciseLogEntry(logEntry: ExerciseLogEntry): Long
}