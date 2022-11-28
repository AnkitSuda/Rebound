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

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.rebound.domain.LogSetType
import com.ankitsuda.rebound.domain.entities.*
import kotlinx.coroutines.flow.Flow
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

    @Query(
        """WITH split(entry_id, personal_records, str) AS (
    SELECT  entry_id, '', personal_records||',' FROM exercise_log_entries JOIN exercise_logs el ON el.id = log_id AND workout_id = :workoutId 
    UNION ALL SELECT  entry_id,
    substr(str, 0, instr(str, ',')),
    substr(str, instr(str, ',')+1)
    FROM split WHERE str !='' 
) SELECT COUNT(personal_records) as PRs FROM split WHERE  personal_records != '';"""
    )
    fun getPRsCountOfEntriesByWorkoutId(workoutId: String): Flow<Int>

    @RawQuery(observedEntities = [Workout::class, ExerciseLogEntry::class, ExerciseWorkoutJunction::class])
    fun getAllWorkoutsRawQuery(query: SupportSQLiteQuery): Flow<List<Workout>>

    @RawQuery(observedEntities = [Workout::class, ExerciseLogEntry::class, ExerciseWorkoutJunction::class])
    fun getAllWorkoutsRawQueryPaged(query: SupportSQLiteQuery): PagingSource<Int, Workout>

    @Query("SELECT COUNT(*) as count, start_at as date FROM workouts WHERE is_hidden = 0 AND in_progress = 0 GROUP BY start_at")
    fun getWorkoutsCount(): Flow<List<CountWithDate>>

    @Query("SELECT COUNT(*) as count, start_at as date FROM workouts WHERE date(start_at / 1000,'unixepoch') >= date(:dateStart / 1000,'unixepoch') AND date(start_at / 1000,'unixepoch') <= date(:dateEnd / 1000,'unixepoch') AND is_hidden = 0 AND in_progress = 0 GROUP BY start_at")
    fun getWorkoutsCountOnDateRange(dateStart: Long, dateEnd: Long): Flow<List<CountWithDate>>

    @Query("""
        SELECT SUM(count) FROM (SELECT COUNT(*) as count FROM workouts WHERE 
date(start_at / 1000,'unixepoch') >= date(:dateStart / 1000,'unixepoch') AND
 date(start_at / 1000,'unixepoch') <= date(:dateEnd / 1000,'unixepoch') 
AND is_hidden = 0 AND in_progress = 0 GROUP BY start_at)
    """)
    fun getWorkoutsCountOnDateRangeAlt(dateStart: Long, dateEnd: Long): Flow<Long>

    @Query("""
        SELECT SUM(count) FROM (SELECT COUNT(*) as count FROM workouts WHERE 
date(start_at / 10000,'unixepoch') >= date(:date / 10000,'unixepoch') AND
 date(start_at / 10000,'unixepoch') <= date(:date / 10000,'unixepoch') 
AND is_hidden = 0 AND in_progress = 0 GROUP BY start_at)
    """)
    fun getWorkoutsCountOnMonth(date: Long): Flow<Long>

    @Query("SELECT * FROM exercise_logs WHERE id = :logId")
    fun getExerciseLogByLogId(logId: String): Flow<ExerciseLog>

    @Transaction
    @Query("SELECT * FROM exercise_workout_junctions j JOIN exercises e ON e.exercise_id = j.exercise_id JOIN muscles m ON m.tag = e.primary_muscle_tag WHERE workout_id = :workoutId")
    fun getLogEntriesWithExtraInfo(workoutId: String): Flow<List<LogEntriesWithExtraInfo>>

    @Query("SELECT COUNT() FROM workouts WHERE is_hidden = 0 AND in_progress = 0")
    fun getTotalWorkoutsCount(): Flow<Long>

    @Query("SELECT e.weight FROM exercise_log_entries e JOIN exercise_logs j ON j.id = e.log_id JOIN workouts w ON w.id = j.workout_id WHERE w.is_hidden = 0 AND w.in_progress = 0 ORDER BY e.weight DESC LIMIT 1")
    fun getMaxWeightLifted(): Flow<Double?>

    @Query("SELECT e.* FROM exercise_log_entries e JOIN exercise_logs j ON j.id = e.log_id JOIN workouts w ON w.id = j.workout_id WHERE w.is_hidden = 0 AND w.in_progress = 0 ORDER BY w.start_at")
    fun getNonHiddenExerciseLogEntries(): Flow<List<ExerciseLogEntry>?>

    @Query("SELECT SUM(completed_at - start_at) as duration FROM workouts WHERE is_hidden = 0 AND in_progress = 0")
    fun getTotalWorkoutsDuration(): Flow<Long>

    @Query("SELECT (completed_at - start_at) as duration FROM workouts WHERE is_hidden = 0 AND in_progress = 0")
    fun getWorkoutsDurationsOnly(): Flow<List<Long>>

    @Query("SELECT e.weight FROM exercise_log_entries e JOIN exercise_logs j ON j.id = e.log_id JOIN workouts w ON w.id = j.workout_id JOIN exercise_workout_junctions ewj ON w.id = ewj.workout_id WHERE ewj.exercise_id = :exerciseId AND w.is_hidden = 0 AND w.in_progress = 0 ORDER BY e.weight DESC LIMIT 1")
    fun getMaxWeightLiftedInExercise(exerciseId: String): Flow<Double?>

    @Query("SELECT completed_at - start_at as duration FROM workouts WHERE is_hidden = 0 AND in_progress = 0 ORDER BY duration DESC LIMIT 1")
    fun getLongestWorkoutDuration(): Flow<Long?>

    @Insert
    suspend fun insertExerciseSetGroupNote(exerciseSetGroupNote: ExerciseSetGroupNote)

    @Query("DELETE FROM exercise_set_group_notes WHERE id = :exerciseSetGroupNoteId")
    suspend fun deleteExerciseSetGroupNote(exerciseSetGroupNoteId: String)

    @Update
    suspend fun updateExerciseSetGroupNote(exerciseSetGroupNote: ExerciseSetGroupNote)

    @Query("UPDATE exercise_workout_junctions SET superset_id = :supersetId WHERE id = :junctionId")
    suspend fun updateExerciseWorkoutJunctionSupersetId(junctionId: String, supersetId: Int?)

    @Query("UPDATE exercise_workout_junctions SET barbell_id = :barbellId WHERE id = :junctionId")
    suspend fun updateExerciseWorkoutJunctionBarbellId(junctionId: String, barbellId: String?)

    @Query(
        """
        SELECT * FROM workouts w
        WHERE
        date(start_at / 1000,'unixepoch') >= date(:dateStart / 1000,'unixepoch') AND date(start_at / 1000,'unixepoch') <= date(:dateEnd / 1000,'unixepoch') 
        AND w.is_hidden = 0 AND w.in_progress = 0 
        ORDER BY w.completed_at DESC
        """
    )
    fun getWorkoutsWithExtraInfoAltPaged(dateStart: Long, dateEnd: Long): PagingSource<Int, WorkoutWithExtraInfoAlt>

    @Query(
        """
        SELECT * FROM workouts w
        WHERE w.is_hidden = 0 AND w.in_progress = 0 
        ORDER BY w.completed_at DESC
        """
    )
    fun getWorkoutsWithExtraInfoAltPaged(): PagingSource<Int, WorkoutWithExtraInfoAlt>

    @Query(
        """
        SELECT * FROM workouts w
        WHERE w.is_hidden = 0 AND w.in_progress = 0 
        ORDER BY w.completed_at DESC
        """
    )
    fun getWorkoutsWithExtraInfoAlt(): Flow<List<WorkoutWithExtraInfoAlt>>

    @Transaction
    suspend fun updateWarmUpSets(
        junction: LogEntriesWithExerciseJunction,
        warmUpSets: List<ExerciseLogEntry>
    ) {
        val time = LocalDateTime.now()

        val junctionId = junction.junction.id

        val sortedEntries = junction.logEntries.sortedWith { left, right ->
            left.setNumber?.compareTo(right.setNumber ?: 0) ?: 0
        }
//        val newEntries = arrayListOf<ExerciseLogEntry>()
        val logsToAdd = arrayListOf<ExerciseLog>()
        val entriesToAdd = arrayListOf<ExerciseLogEntry>()
        val entriesToDelete = arrayListOf<ExerciseLogEntry>()
        val entriesToUpdate = arrayListOf<ExerciseLogEntry>()

        var mSetNumber = 0

        for (set in warmUpSets) {
            val entryId = generateId()
            val logId = generateId()

            val newEntry =
                set.copy(
                    entryId = entryId,
                    logId = logId,
                    junctionId = junctionId,
                    setNumber = mSetNumber + 1,
                    createdAt = time,
                    updatedAt = time,
                )

//            newEntries.add(
//                newEntry
//            )
            entriesToAdd.add(newEntry)
            logsToAdd.add(
                ExerciseLog(
                    id = logId,
                    workoutId = junction.junction.workoutId,
                    createdAt = time,
                    updatedAt = time,
                )
            )
            mSetNumber++
        }

        for (entry in sortedEntries) {
            if (entry.setType == LogSetType.WARM_UP) {
                entriesToDelete.add(
                    entry
                )
            } else {
                val updatedEntry = entry.copy(
                    setNumber = mSetNumber + 1,
                    updatedAt = time
                )

//                newEntries.add(updatedEntry)

                entriesToUpdate.add(
                    updatedEntry
                )
                mSetNumber++
            }
        }



        deleteExerciseLogEntries(entriesToDelete.map { it.entryId })
        deleteExerciseLogs(entriesToDelete.filter { it.logId != null }.map { it.logId!! })

        for (logToAdd in logsToAdd) {
            insertExerciseLog(logToAdd)
        }

        for (entryToAdd in entriesToAdd) {
            insertExerciseLogEntry(entryToAdd)
        }

        for (entryToUpdate in entriesToUpdate) {
            updateExerciseLogEntry(entryToUpdate)
        }
    }
}