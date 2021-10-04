package com.ankitsuda.rebound.data.repositories

import com.ankitsuda.rebound.data.daos.ExercisesDao
import com.ankitsuda.rebound.data.entities.Exercise
import org.threeten.bp.OffsetDateTime
import java.util.*
import javax.inject.Inject

class ExercisesRepository @Inject constructor(private val exercisesDao: ExercisesDao) {

    fun getExercise(exerciseId: Long) = exercisesDao.getSingleExercise(exerciseId)

    fun getAllLogEntries(exerciseId: Long) = exercisesDao.getAllLogEntries(exerciseId)

    fun getAllExercises() = exercisesDao.getAllExercises()
    fun getAllExercisesWithMuscles() = exercisesDao.getAllExercisesWithMuscles()

    suspend fun createExercise(exercise: Exercise) {
        exercisesDao.insertExercise(
            exercise.copy(
                createdAt = Date(),
                updatedAt = Date()
            )
        )
    }

}