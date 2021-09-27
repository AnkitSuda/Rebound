package com.ankitsuda.rebound.data.repositories

import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.daos.WorkoutsDao
import com.ankitsuda.rebound.data.datastore.PrefStorage
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WorkoutsRepository @Inject constructor(
    private val workoutsDao: WorkoutsDao,
    private val prefStorage: PrefStorage
) {

    fun getCurrentWorkoutId() = prefStorage.currentWorkoutId

    suspend fun setCurrentWorkoutId(value: Long) {
        prefStorage.setCurrentWorkoutId(value)
    }

    suspend fun cancelCurrentWorkout() {
        prefStorage.setCurrentWorkoutId(-1)
    }
}