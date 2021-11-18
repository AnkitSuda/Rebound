package com.ankitsuda.rebound.data.repositories

import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.db.daos.ExercisesDao
import com.ankitsuda.rebound.data.db.daos.MeasurementsDao
import com.ankitsuda.rebound.data.db.daos.MusclesDao
import com.ankitsuda.rebound.data.db.daos.WorkoutsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoriesModule {

    @Singleton
    @Provides
    fun provideMeasurementsRepository(measurementsDao: MeasurementsDao) =
        MeasurementsRepository(measurementsDao)

    @Singleton
    @Provides
    fun provideWorkoutsRepository(workoutsDao: WorkoutsDao, prefStorage: PrefStorage) =
        WorkoutsRepository(workoutsDao, prefStorage)

    @Singleton
    @Provides
    fun provideMusclesRepository(musclesDao: MusclesDao) =
        MusclesRepository(musclesDao)

    @Singleton
    @Provides
    fun provideExercisesRepository(exercisesDao: ExercisesDao) =
        ExercisesRepository(exercisesDao)
}