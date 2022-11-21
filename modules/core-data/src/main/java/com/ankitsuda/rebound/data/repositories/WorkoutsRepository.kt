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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.base.utils.toEpochMillis
import com.ankitsuda.rebound.data.db.daos.WorkoutsDao
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.db.daos.WorkoutTemplatesDao
import com.ankitsuda.rebound.domain.MaxDurationPR
import com.ankitsuda.rebound.domain.MaxWeightPR
import com.ankitsuda.rebound.domain.PersonalRecord
import com.ankitsuda.rebound.domain.addIfNot
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
    private val templatesDao: WorkoutTemplatesDao,
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
            personalRecords = null,
            completedAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ) ?: return

        val prs = arrayListOf<PersonalRecord>()

        val lastMaxDuration = workoutsDao.getLongestWorkoutDuration().firstOrNull()

        if (lastMaxDuration == null || (updatedWorkout.getDuration() ?: 0) > lastMaxDuration) {
            prs.addIfNot(MaxDurationPR())
        }

        updatedWorkout.personalRecords = prs

        val junctions = getLogEntriesWithExerciseJunction(workoutId).first()

        for (j in junctions) {
            val lastMaxWeightInExercise =
                getMaxWeightLiftedInExercise(j.exercise.exerciseId).firstOrNull() ?: 0.0

            j.logEntries.sortedByDescending { it.weight }.getOrNull(0)?.let { maxWeightEntry ->
                if ((maxWeightEntry.weight ?: 0.0) > lastMaxWeightInExercise) {
                    val entryPrs = arrayListOf<PersonalRecord>()
                    maxWeightEntry.personalRecords?.let { entryPrs.addAll(it) }
                    entryPrs.add(MaxWeightPR())
                    updateExerciseLogEntry(
                        maxWeightEntry.copy(
                            personalRecords = entryPrs
                        )
                    )
                }
            }
        }

        updateWorkout(updatedWorkout)
    }

    fun getExercisesCountByWorkoutId(workoutId: String) =
        workoutsDao.getExercisesCountByWorkoutId(workoutId)

    fun getPRsCountOfEntriesByWorkoutId(workoutId: String) =
        workoutsDao.getPRsCountOfEntriesByWorkoutId(workoutId)

//    fun getTotalVolumeLiftedByWorkoutId(workoutId: String) =
//        workoutsDao.getTotalVolumeOfWorkout(workoutId = workoutId)

    fun getTotalVolumeLiftedByWorkoutId(workoutId: String): Flow<Double> =
        workoutsDao.getLogEntriesByWorkoutId(workoutId).map {
            it.calculateTotalVolume()
        }

    private suspend fun getWorkoutExtraInfo(workout: Workout): WorkoutWithExtraInfo {
        val totalVolume =
            getTotalVolumeLiftedByWorkoutId(workoutId = workout.id).firstOrNull()

        val totalExercises =
            getExercisesCountByWorkoutId(workoutId = workout.id).firstOrNull()

        var totalPRs = 0

        val totalPRsOfEntries =
            getPRsCountOfEntriesByWorkoutId(workoutId = workout.id).firstOrNull()

        totalPRs += workout.personalRecords?.size ?: 0
        totalPRs += totalPRsOfEntries ?: 0


        return WorkoutWithExtraInfo(
            workout = workout,
            totalVolume = totalVolume,
            totalExercises = totalExercises,
            totalPRs = totalPRs
        )
    }

    private suspend fun getWorkoutsExtraInfo(workouts: List<Workout>): List<WorkoutWithExtraInfo> {
        val mWorkouts = arrayListOf<WorkoutWithExtraInfo>()
        for (workout in workouts) {
            mWorkouts.add(
                getWorkoutExtraInfo(workout)
            )
        }

        return mWorkouts.toList()
    }

    fun getWorkoutsWithExtraInfo(date: LocalDate? = null) =
        workoutsDao.getAllWorkoutsRawQuery(
            SimpleSQLiteQuery(
                "SELECT * FROM workouts WHERE ${if (date != null) "date(created_at / 1000,'unixepoch') = date(? / 1000,'unixepoch') AND" else ""} is_hidden = 0 AND in_progress = 0 ORDER BY completed_at DESC",
                if (date != null) arrayOf<Any>(date.toEpochMillis()) else arrayOf()
            )
        ).map {
            getWorkoutsExtraInfo(it)
        }

//    fun getWorkoutsWithExtraInfoAlt() = workoutsDao.getWorkoutsWithExtraInfoAlt()

    fun getWorkoutsWithExtraInfoPaged(
        dateStart: LocalDate? = null,
        dateEnd: LocalDate? = null
    ) =
        Pager(PagingConfig(pageSize = 15)) {
            if (dateStart != null && dateEnd != null) {
                workoutsDao.getWorkoutsWithExtraInfoAltPaged(
                    dateStart.toEpochMillis(),
                    dateEnd.toEpochMillis()
                )
            } else {
                workoutsDao.getWorkoutsWithExtraInfoAltPaged()
            }
        }
            .flow
            .map {
                it.map { item ->
                    val logEntries = item.junctions?.flatMap { j -> j.logEntries }
                    WorkoutWithExtraInfo(
                        workout = item.workout,
                        totalVolume = logEntries?.calculateTotalVolume(),
                        totalExercises = item.junctions?.size,
                        totalPRs = logEntries?.getTotalPRs(item.workout?.personalRecords?.size)
                    )
                }
            }

    fun getWorkoutsWithExtraInfo() =
        workoutsDao.getWorkoutsWithExtraInfoAlt()
            .map {
                it.map { item ->
                    val logEntries = item.junctions?.flatMap { j -> j.logEntries }
                    WorkoutWithExtraInfo(
                        workout = item.workout,
                        totalVolume = logEntries?.calculateTotalVolume(),
                        totalExercises = item.junctions?.size,
                        totalPRs = logEntries?.getTotalPRs(item.workout?.personalRecords?.size)
                    )
                }
            }

    fun getWorkoutsCount() =
        workoutsDao.getWorkoutsCount()

    fun getWorkoutsCountOnDateRange(dateStart: LocalDate, dateEnd: LocalDate) =
        workoutsDao.getWorkoutsCountOnDateRange(
            dateStart = dateStart.toEpochMillis(),
            dateEnd = dateEnd.toEpochMillis()
        )

    fun getWorkoutsCountOnMonthOnDateRangeAlt(dateStart: LocalDate, dateEnd: LocalDate) =
        workoutsDao.getWorkoutsCountOnMonthOnDateRangeAlt(
            dateStart = dateStart.toEpochMillis(),
            dateEnd = dateEnd.toEpochMillis()
        )

    fun getWorkoutsCountOnMonth(date: Long) =
        workoutsDao.getWorkoutsCountOnMonth(
            date = date
        )

    fun getExerciseLogByLogId(logId: String) = workoutsDao.getExerciseLogByLogId(logId)

    private suspend fun checkIfWorkoutIsActive(discardActive: Boolean): Boolean {
        val activeWorkoutId = getCurrentWorkoutId().firstOrNull()
        return if (activeWorkoutId == null || activeWorkoutId == NONE_WORKOUT_ID) {
            false
        } else {
            if (discardActive) {
                setCurrentWorkoutId(NONE_WORKOUT_ID)
                deleteWorkoutWithEverything(activeWorkoutId)
                false
            } else {
                true
            }
        }
    }

    suspend fun startWorkoutFromTemplate(
        templateId: String,
        discardActive: Boolean,
        onWorkoutAlreadyActive: () -> Unit
    ) {
        val isActive = checkIfWorkoutIsActive(discardActive = discardActive);

        if (isActive) {
            onWorkoutAlreadyActive()
            return
        }

        val template = templatesDao.getTemplate(templateId).firstOrNull() ?: return;

        if (template.workoutId == null) return

        templatesDao.updateTemplate(
            template.copy(
                lastPerformedAt = LocalDateTime.now()
            )
        )

        startWorkoutFromWorkout(template.workoutId!!)
    }

    suspend fun startWorkoutFromWorkout(
        workoutId: String,
        discardActive: Boolean,
        onWorkoutAlreadyActive: () -> Unit
    ) {
        val isActive = checkIfWorkoutIsActive(discardActive = discardActive);

        if (isActive) {
            onWorkoutAlreadyActive();
            return;
        }

        startWorkoutFromWorkout(workoutId)
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
                    personalRecords = null,
                    completed = false,
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

    fun getTotalWorkoutsCount(): Flow<Long> = workoutsDao.getTotalWorkoutsCount()

    fun getMaxWeightLifted(): Flow<Double?> = workoutsDao.getMaxWeightLifted()

    fun getMaxWeightLiftedInExercise(exerciseId: String): Flow<Double?> =
        workoutsDao.getMaxWeightLiftedInExercise(exerciseId)

    fun getNonHiddenExerciseLogEntries(): Flow<List<ExerciseLogEntry>?> =
        workoutsDao.getNonHiddenExerciseLogEntries()

    fun getTotalWorkoutsDuration(): Flow<Long> =
        workoutsDao.getTotalWorkoutsDuration()

    fun getWorkoutsDurationsOnly(): Flow<List<Long>> =
        workoutsDao.getWorkoutsDurationsOnly()

    suspend fun updateWarmUpSets(
        junction: LogEntriesWithExerciseJunction,
        sets: List<ExerciseLogEntry>
    ) {
        workoutsDao.updateWarmUpSets(junction, sets)
    }

    suspend fun addExerciseSetGroupNote(exerciseSetGroupNote: ExerciseSetGroupNote) {
        workoutsDao.insertExerciseSetGroupNote(exerciseSetGroupNote)
    }

    suspend fun deleteExerciseSetGroupNote(exerciseSetGroupNoteId: String) {
        workoutsDao.deleteExerciseSetGroupNote(exerciseSetGroupNoteId)
    }

    suspend fun updateExerciseSetGroupNote(exerciseSetGroupNote: ExerciseSetGroupNote) {
        workoutsDao.updateExerciseSetGroupNote(exerciseSetGroupNote)
    }

    suspend fun updateExerciseWorkoutJunctionSupersetId(junctionId: String, supersetId: Int?) {
        workoutsDao.updateExerciseWorkoutJunctionSupersetId(junctionId, supersetId)
    }
}