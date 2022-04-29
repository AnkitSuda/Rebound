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

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.ankitsuda.rebound.domain.entities.*
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface WorkoutsDao {

    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    fun getWorkout(workoutId: String): Flow<Workout?>

    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE date(created_at / 1000,'unixepoch') = date(:date / 1000,'unixepoch') AND is_hidden = 0 AND in_progress = 0")
    fun getAllWorkoutsOnDate(date: Long): Flow<List<Workout>>

    @Query("SELECT * FROM exercise_workout_junctions WHERE workout_id = :workoutId")
    fun getExerciseWorkoutJunction(workoutId: String): Flow<List<ExerciseWorkoutJunction>>

    @Query("SELECT * FROM exercise_workout_junctions WHERE workout_id = :workoutId")
    suspend fun getExerciseWorkoutJunctionsNonFlow(workoutId: String): List<ExerciseWorkoutJunction>

    @Query("SELECT * FROM exercise_log_entries WHERE junction_id = :junctionId")
    suspend fun getExerciseLogEntriesNonFlow(junctionId: String): List<ExerciseLogEntry>

    @Query("DELETE FROM exercise_log_entries WHERE junction_id IN (:junctionIds)")
    suspend fun deleteAllLogEntriesForJunctionIds(junctionIds: List<String>)

    @Query("DELETE FROM exercise_logs WHERE workout_id = :workoutId")
    suspend fun deleteAllLogsForWorkoutId(workoutId: String)

    @Transaction
    @Query("SELECT * FROM exercise_workout_junctions WHERE workout_id = :workoutId")
    fun getLogEntriesWithExerciseJunction(workoutId: String): Flow<List<LogEntriesWithExerciseJunction>>

    @Transaction
    @Query("SELECT exercise_log_entries.* FROM exercise_log_entries JOIN exercise_workout_junctions j WHERE j.workout_id = :workoutId AND j.id = junction_id")
    fun getLogEntriesByWorkoutId(workoutId: String): Flow<List<ExerciseLogEntry>>

    @Delete
    suspend fun deleteExerciseLogEntry(exerciseLogEntry: ExerciseLogEntry)

    @Query("DELETE FROM exercise_log_entries WHERE entry_id = :entryId")
    suspend fun deleteExerciseLogEntryById(entryId: String)

    @Query("DELETE FROM exercise_log_entries WHERE entry_id IN (:ids)")
    suspend fun deleteExerciseLogEntries(ids: List<String>)

    @Delete
    suspend fun deleteExerciseLog(exerciseLog: ExerciseLog)


    @Query("DELETE FROM exercise_logs WHERE id IN (:ids)")
    suspend fun deleteExerciseLogs(ids: List<String>)

    @Delete
    suspend fun deleteExerciseWorkoutJunction(exerciseWorkoutJunction: ExerciseWorkoutJunction)

    @Query("DELETE FROM exercise_workout_junctions WHERE id IN (:ids)")
    suspend fun deleteExerciseWorkoutJunctions(ids: List<String>)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteWorkoutById(workoutId: String)

    @Update
    suspend fun updateExerciseLogEntry(exerciseLogEntry: ExerciseLogEntry)

    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Insert
    suspend fun insertExerciseWorkoutJunction(exerciseWorkoutJunction: ExerciseWorkoutJunction)

    @Insert
    suspend fun insertExerciseLog(log: ExerciseLog)

    @Insert
    suspend fun insertExerciseLogEntry(logEntry: ExerciseLogEntry)

    @Transaction
    suspend fun reorderEntriesGroupByDelete(
        entriesGroup: ArrayList<ExerciseLogEntry>,
        entryToDelete: ExerciseLogEntry
    ) {
        deleteExerciseLogEntryById(entryToDelete.entryId)

        entriesGroup.remove(entriesGroup.find { it.entryId == entryToDelete.entryId })

        for (groupEntry in entriesGroup) {
            val index = entriesGroup.indexOf(groupEntry)
            updateExerciseLogEntry(
                groupEntry.copy(
                    setNumber = index + 1,
                    updatedAt = LocalDateTime.now()
                )
            )
        }
    }

    @Query("SELECT SUM(volume) FROM (SELECT SUM(weight) * SUM(reps) AS volume FROM exercise_log_entries WHERE junction_id IN (SELECT id FROM exercise_workout_junctions WHERE workout_id = :workoutId) GROUP BY junction_id)")
    fun getTotalVolumeOfWorkout(workoutId: String): Flow<Float>

    @Query("SELECT COUNT(*) FROM exercise_workout_junctions WHERE workout_id = :workoutId")
    fun getExercisesCountByWorkoutId(workoutId: String): Flow<Int>

    @RawQuery(observedEntities = [Workout::class, ExerciseLogEntry::class, ExerciseWorkoutJunction::class])
    fun getAllWorkoutsRawQuery(query: SupportSQLiteQuery): Flow<List<Workout>>

    @Query("SELECT COUNT(*) as count, start_at as date FROM workouts WHERE date(start_at / 1000,'unixepoch') >= date(:dateStart / 1000,'unixepoch') AND date(start_at / 1000,'unixepoch') <= date(:dateEnd / 1000,'unixepoch') AND is_hidden = 0 AND in_progress = 0 GROUP BY start_at")
    fun getWorkoutsCountOnDateRange(dateStart: Long, dateEnd: Long): Flow<List<CountWithDate>>

    @Query("SELECT * FROM exercise_logs WHERE id = :logId")
    fun getExerciseLogByLogId(logId: String): Flow<ExerciseLog>

    @Transaction
    @Query("SELECT * FROM exercise_workout_junctions j JOIN exercises e ON e.exercise_id = j.exercise_id JOIN muscles m ON m.tag = e.primary_muscle_tag WHERE workout_id = :workoutId")
    fun getLogEntriesWithExtraInfo(workoutId: String): Flow<List<LogEntriesWithExtraInfo>>
}