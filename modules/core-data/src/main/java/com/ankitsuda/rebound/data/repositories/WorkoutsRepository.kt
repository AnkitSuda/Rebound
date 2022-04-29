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

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.base.utils.toEpochMillis
import com.ankitsuda.rebound.data.db.daos.WorkoutsDao
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.domain.entities.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class WorkoutsRepository @Inject constructor(
    private val workoutsDao: WorkoutsDao,
    private val prefStorage: PrefStorage
) {
    fun getCurrentWorkoutId() = prefStorage.currentWorkoutId


    fun getWorkout(workoutId: String) = workoutsDao.getWorkout(workoutId)

    fun getAllWorkoutsOnDate(date: LocalDate): Flow<List<Workout>> {
        val epoch = date.toEpochMillis()
        Timber.d("epoch $epoch")
        Timber.d("date $date")
        return workoutsDao.getAllWorkoutsOnDate(epoch).map {
            Timber.d("list $it")
            it
        }
    }

    fun getExerciseWorkoutJunctions(workoutId: String) =
        workoutsDao.getExerciseWorkoutJunction(workoutId)

    fun getLogEntriesWithExerciseJunction(workoutId: String) =
        workoutsDao.getLogEntriesWithExerciseJunction(workoutId)

    suspend fun updateWorkout(workout: Workout) {
        workoutsDao.updateWorkout(workout.copy(updatedAt = LocalDateTime.now()))
    }

    suspend fun createWorkout(workout: Workout): String {
        val newId = generateId()
        workoutsDao.insertWorkout(
            workout.copy(
                id = newId,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
        return newId
    }

    suspend fun setCurrentWorkoutId(value: String) {
        prefStorage.setCurrentWorkoutId(value)
    }

    suspend fun deleteWorkoutWithEverything(workoutId: String) {
        val workout = getWorkout(workoutId).firstOrNull()
        workout?.let {
            deleteWorkoutWithEverything(it)
        }
    }

    /**
     * Deletes everything related to workout
     */
    suspend fun deleteWorkoutWithEverything(workout: Workout) {
        // Get all ExerciseWorkoutJunctions related to workout
        val junctions = workoutsDao.getExerciseWorkoutJunctionsNonFlow(workout.id!!)
        // Delete all ExerciseLogEntries for workout using junction ids
        workoutsDao.deleteAllLogEntriesForJunctionIds(junctionIds = junctions.map { it.id })
        // Delete all ExerciseLogs for workout
        workoutsDao.deleteAllLogsForWorkoutId(workoutId = workout.id!!)
        // Delete all junctions related to workout
        workoutsDao.deleteExerciseWorkoutJunctions(junctions.map { it.id })
        // Delete workout
        workoutsDao.deleteWorkout(workout)
    }

    suspend fun addExerciseToWorkout(workoutId: String, exerciseId: String) {
        workoutsDao.insertExerciseWorkoutJunction(
            ExerciseWorkoutJunction(
                id = generateId(),
                workoutId = workoutId,
                exerciseId = exerciseId
            )
        )
    }

    suspend fun addEmptySetToExercise(
        setNumber: Int,
        exerciseWorkoutJunction: ExerciseWorkoutJunction
    ): ExerciseLogEntry {
        val logId = generateId()
        workoutsDao.insertExerciseLog(
            ExerciseLog(
                id = logId,
                workoutId = exerciseWorkoutJunction.workoutId,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        )

        val entry = ExerciseLogEntry(
            entryId = generateId(),
            logId = logId,
            junctionId = exerciseWorkoutJunction.id,
            setNumber = setNumber,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        workoutsDao.insertExerciseLogEntry(entry)

        return entry
    }

    suspend fun updateExerciseLogEntry(entry: ExerciseLogEntry) {
        workoutsDao.updateExerciseLogEntry(entry.copy(updatedAt = LocalDateTime.now()))
    }

    suspend fun reorderEntriesGroupByDelete(
        entriesGroup: ArrayList<ExerciseLogEntry>,
        entryToDelete: ExerciseLogEntry
    ) {
        workoutsDao.reorderEntriesGroupByDelete(
            entriesGroup = entriesGroup,
            entryToDelete = entryToDelete
        )
    }

    suspend fun deleteExerciseLogEntry(entry: ExerciseLogEntry) {
        workoutsDao.deleteExerciseLogEntry(entry)
    }

    suspend fun deleteExerciseFromWorkout(logEntriesWithJunctionItem: LogEntriesWithExerciseJunction) {
        with(logEntriesWithJunctionItem) {
            workoutsDao.deleteExerciseWorkoutJunction(junction)
            workoutsDao.deleteExerciseLogEntries(logEntries.map { it.entryId })
            workoutsDao.deleteExerciseLogs(logEntries.map { it.logId!! })
        }
    }

    suspend fun finishWorkout(workoutId: String) {
        val workout = getWorkout(workoutId).first()
        val updatedWorkout = workout?.copy(
            inProgress = false,
            isHidden = false,
            completedAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        if (updatedWorkout != null) {
            updateWorkout(updatedWorkout)
        }
    }

    fun getExercisesCountByWorkoutId(workoutId: String) =
        workoutsDao.getExercisesCountByWorkoutId(workoutId)

//    fun getTotalVolumeLiftedByWorkoutId(workoutId: String) =
//        workoutsDao.getTotalVolumeOfWorkout(workoutId = workoutId)

    fun getTotalVolumeLiftedByWorkoutId(workoutId: String): Flow<Float> =
        workoutsDao.getLogEntriesByWorkoutId(workoutId).map {
            it.calculateTotalVolume()
        }


    fun getWorkoutsWithExtraInfo(date: LocalDate) =
        workoutsDao.getAllWorkoutsRawQuery(
            SimpleSQLiteQuery(
                "SELECT * FROM workouts WHERE date(created_at / 1000,'unixepoch') = date(? / 1000,'unixepoch') AND is_hidden = 0 AND in_progress = 0",
                arrayOf<Any>(date.toEpochMillis())
            )
        ).map {
            val mWorkouts = arrayListOf<WorkoutWithExtraInfo>()
            for (workout in it) {
                val totalVolume =
                    getTotalVolumeLiftedByWorkoutId(workoutId = workout.id).firstOrNull()
                val totalExercises =
                    getExercisesCountByWorkoutId(workoutId = workout.id).firstOrNull()

                mWorkouts.add(
                    WorkoutWithExtraInfo(
                        workout = workout,
                        totalVolume = totalVolume,
                        totalExercises = totalExercises
                    )
                )
            }

            mWorkouts.toList()
        }

    fun getWorkoutsCountOnDateRange(dateStart: LocalDate, dateEnd: LocalDate) =
        workoutsDao.getWorkoutsCountOnDateRange(
            dateStart = dateStart.toEpochMillis(),
            dateEnd = dateEnd.toEpochMillis()
        )

    fun getExerciseLogByLogId(logId: String) = workoutsDao.getExerciseLogByLogId(logId)


    suspend fun startWorkoutFromWorkout(
        workoutId: String,
        discardActive: Boolean,
        onWorkoutAlreadyActive: () -> Unit
    ) {
        val activeWorkoutId = getCurrentWorkoutId().firstOrNull()
        if (activeWorkoutId == null || activeWorkoutId == NONE_WORKOUT_ID) {
            startWorkoutFromWorkout(workoutId)
        } else {
            if (discardActive) {
                setCurrentWorkoutId(NONE_WORKOUT_ID)
                deleteWorkoutWithEverything(activeWorkoutId)
                startWorkoutFromWorkout(workoutId)
            } else {
                onWorkoutAlreadyActive()
            }
        }
    }

    private suspend fun startWorkoutFromWorkout(workoutId: String) {
        val fromWorkout = getWorkout(workoutId).first()

        if (fromWorkout == null) {
            Timber.e("Workout with id $workoutId not found")
            return
        }

        val exerciseWorkoutJunctionsToAdd = arrayListOf<ExerciseWorkoutJunction>()
        val exerciseLogsToAdd = arrayListOf<ExerciseLog>()
        val exerciseLogEntriesToAdd = arrayListOf<ExerciseLogEntry>()

        val newWorkoutId = generateId()
        val currentDateTime = LocalDateTime.now()
        val newWorkout = fromWorkout.copy(
            id = newWorkoutId,
            isHidden = true,
            inProgress = true,
            startAt = currentDateTime,
            createdAt = currentDateTime,
            updatedAt = currentDateTime
        )

        val fromWithJunctions = getLogEntriesWithExerciseJunction(workoutId).first()

        for (withJunction in fromWithJunctions) {
            val newJunctionId = generateId()
            val newJunction = withJunction.junction.copy(
                id = newJunctionId,
                workoutId = newWorkoutId
            )

            exerciseWorkoutJunctionsToAdd.add(newJunction)

            for (entry in withJunction.logEntries) {
                val newEntryId = generateId()

                val newLogId = if (entry.logId != null) {
                    val newExerciseLogId = generateId()
                    val oldExerciseLog = getExerciseLogByLogId(entry.logId!!).first()
                    val newExerciseLog = oldExerciseLog.copy(
                        id = newExerciseLogId,
                        workoutId = newWorkoutId,
                        createdAt = currentDateTime,
                        updatedAt = currentDateTime
                    )
                    exerciseLogsToAdd.add(newExerciseLog)
                    newExerciseLogId
                } else {
                    null
                }

                val newEntry = entry.copy(
                    entryId = newEntryId,
                    logId = newLogId,
                    junctionId = newJunctionId,
                    createdAt = currentDateTime,
                    updatedAt = currentDateTime
                )

                exerciseLogEntriesToAdd.add(newEntry)
            }

        }

        // Insert to db
        workoutsDao.insertWorkout(newWorkout)

        for (junction in exerciseWorkoutJunctionsToAdd) {
            workoutsDao.insertExerciseWorkoutJunction(junction)
        }

        for (exerciseLog in exerciseLogsToAdd) {
            workoutsDao.insertExerciseLog(exerciseLog)
        }

        for (entry in exerciseLogEntriesToAdd) {
            workoutsDao.insertExerciseLogEntry(entry)
        }

        setCurrentWorkoutId(newWorkoutId)
    }

    fun getLogEntriesWithExtraInfo(workoutId: String): Flow<List<LogEntriesWithExtraInfo>> =
        workoutsDao.getLogEntriesWithExtraInfo(workoutId)
}