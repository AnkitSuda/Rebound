package com.ankitsuda.rebound.data.repositories

import com.ankitsuda.rebound.data.daos.WorkoutsDao
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.entities.ExerciseLog
import com.ankitsuda.rebound.data.entities.ExerciseLogEntry
import com.ankitsuda.rebound.data.entities.ExerciseWorkoutJunction
import com.ankitsuda.rebound.data.entities.Workout
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import javax.inject.Inject

class WorkoutsRepository @Inject constructor(
    private val workoutsDao: WorkoutsDao,
    private val prefStorage: PrefStorage
) {
    fun getCurrentWorkoutId() = prefStorage.currentWorkoutId


    fun getWorkout(workoutId: Long) = workoutsDao.getWorkout(workoutId)

    fun getAllWorkoutsOnDate(date: OffsetDateTime) = workoutsDao.getAllWorkoutsOnDate(
        date.format(DateTimeFormatter.ISO_LOCAL_DATE).also {
            Timber.d("Formatted date for $it")
        }
    )

    fun getExerciseWorkoutJunctions(workoutId: Long) =
        workoutsDao.getExerciseWorkoutJunction(workoutId)

    fun getLogEntriesWithExerciseJunction(workoutId: Long) =
        workoutsDao.getLogEntriesWithExerciseJunction(workoutId)

    suspend fun updateWorkout(workout: Workout) {
        workoutsDao.updateWorkout(workout.copy(updatedAt = OffsetDateTime.now()))
    }

    suspend fun createWorkout(workout: Workout): Long {
        return workoutsDao.insertWorkout(workout.copy(createdAt = OffsetDateTime.now()))
    }

    suspend fun setCurrentWorkoutId(value: Long) {
        prefStorage.setCurrentWorkoutId(value)
    }

    suspend fun cancelCurrentWorkout() {
        prefStorage.setCurrentWorkoutId(-1)
    }

    suspend fun addExerciseToWorkout(workoutId: Long, exerciseId: Long) {
        workoutsDao.insertExerciseWorkoutJunction(
            ExerciseWorkoutJunction(
                workoutId = workoutId,
                exerciseId = exerciseId
            )
        )
    }

    suspend fun addEmptySetToExercise(
        setNumber: Int,
        exerciseWorkoutJunction: ExerciseWorkoutJunction
    ): ExerciseLogEntry {
        val logId = workoutsDao.insertExerciseLog(
            ExerciseLog(
                workoutId = exerciseWorkoutJunction.workoutId,
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now(),
            )
        )

        val entry = ExerciseLogEntry(
            logId = logId,
            junctionId = exerciseWorkoutJunction.id,
            setNumber = setNumber,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )

        val entryId = workoutsDao.insertExerciseLogEntry(entry)

        return entry.copy(entryId = entryId)
    }

    suspend fun updateExerciseLogEntry(entry: ExerciseLogEntry) {
        workoutsDao.updateExerciseLogEntry(entry.copy(updatedAt = OffsetDateTime.now()))
    }

    suspend fun deleteExerciseLogEntry(entry: ExerciseLogEntry) {
        workoutsDao.deleteExerciseLogEntry(entry)
    }
}