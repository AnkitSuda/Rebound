package com.ankitsuda.rebound.data.daos

import androidx.room.*
import com.ankitsuda.rebound.data.entities.*
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.OffsetDateTime

@Dao
interface WorkoutsDao {

    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    fun getWorkout(workoutId: Long): Flow<Workout?>

    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE date(created_at) = date(:date)")
    fun getAllWorkoutsOnDate(date: String): Flow<List<Workout>>

    @Query("SELECT * FROM exercise_workout_junctions WHERE workout_id = :workoutId")
    fun getExerciseWorkoutJunction(workoutId: Long): Flow<List<ExerciseWorkoutJunction>>

    @Query("SELECT * FROM exercise_workout_junctions WHERE workout_id = :workoutId")
    suspend fun getExerciseWorkoutJunctionsNonFlow(workoutId: Long): List<ExerciseWorkoutJunction>

    @Query("SELECT * FROM exercise_log_entries WHERE junction_id = :junctionId")
    suspend fun getExerciseLogEntriesNonFlow(junctionId: Long): List<ExerciseLogEntry>

    @Query("DELETE FROM exercise_log_entries WHERE junction_id IN (:junctionIds)")
    suspend fun deleteAllLogEntriesForJunctionIds(junctionIds: List<Long>)

    @Query("DELETE FROM exercise_logs WHERE workout_id = :workoutId")
    suspend fun deleteAllLogsForWorkoutId(workoutId: Long)

    @Transaction
    @Query("SELECT * FROM exercise_workout_junctions WHERE workout_id = :workoutId")
    fun getLogEntriesWithExerciseJunction(workoutId: Long): Flow<List<LogEntriesWithExerciseJunction>>

    @Delete
    suspend fun deleteExerciseLogEntry(exerciseLogEntry: ExerciseLogEntry)

    @Query("DELETE FROM exercise_log_entries WHERE entry_id IN (:ids)")
    suspend fun deleteExerciseLogEntries(ids: List<Long>)

    @Delete
    suspend fun deleteExerciseLog(exerciseLog: ExerciseLog)


    @Query("DELETE FROM exercise_logs WHERE id IN (:ids)")
    suspend fun deleteExerciseLogs(ids: List<Long>)

    @Delete
    suspend fun deleteExerciseWorkoutJunction(exerciseWorkoutJunction: ExerciseWorkoutJunction)

    @Query("DELETE FROM exercise_workout_junctions WHERE id IN (:ids)")
    suspend fun deleteExerciseWorkoutJunctions(ids: List<Long>)


    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteWorkoutById(workoutId: Long)

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