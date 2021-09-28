package com.ankitsuda.rebound.data.repositories

import com.ankitsuda.rebound.data.daos.ExercisesDao
import com.ankitsuda.rebound.data.entities.Exercise
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class ExercisesRepository @Inject constructor(private val exercisesDao: ExercisesDao) {

    suspend fun createExercise(exercise: Exercise) {
        exercisesDao.insertExercise(
            exercise.copy(
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now()
            )
        )
    }

}