package com.ankitsuda.rebound.data.repositories

import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.daos.WorkoutsDao
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.entities.ExerciseWorkoutJunction
import com.ankitsuda.rebound.data.entities.Workout
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class WorkoutsRepository @Inject constructor(
    private val workoutsDao: WorkoutsDao,
    private val prefStorage: PrefStorage
) {
    fun getCurrentWorkoutId() = prefStorage.currentWorkoutId


    fun getWorkout(workoutId: Long) = workoutsDao.getWorkout(workoutId)

    fun getExerciseWorkoutJunctions(workoutId: Long) = workoutsDao.getExerciseWorkoutJunction(workoutId)

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
}