package com.ankitsuda.rebound.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.ankitsuda.base.initializers.AppInitializers
import com.ankitsuda.base.initializers.TimberInitializer
import com.ankitsuda.base.utils.CoroutineDispatchers
import com.ankitsuda.rebound.data.AppDatabase
import com.ankitsuda.rebound.data.daos.ExercisesDao
import com.ankitsuda.rebound.data.daos.MeasurementsDao
import com.ankitsuda.rebound.data.daos.MusclesDao
import com.ankitsuda.rebound.data.daos.WorkoutsDao
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import com.ankitsuda.rebound.data.repositories.MeasurementsRepository
import com.ankitsuda.rebound.data.repositories.MusclesRepository
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun coroutineDispatchers() = CoroutineDispatchers(
        network = Dispatchers.IO,
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @Provides
    fun appContext(app: Application): Context = app.applicationContext

    @Provides
    fun appResources(app: Application): Resources = app.resources

    @Provides
    fun appInitializers(
        timberManager: TimberInitializer,
    ): AppInitializers {
        return AppInitializers(
            timberManager,
        )
    }
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideMeasurementsDao(db: AppDatabase) = db.measurementsDao()

    @Singleton
    @Provides
    fun provideWorkoutsDao(db: AppDatabase) = db.workoutsDao()

    @Singleton
    @Provides
    fun provideMusclesDao(db: AppDatabase) = db.musclesDao()

    @Singleton
    @Provides
    fun provideExercisesDao(db: AppDatabase) = db.exercisesDao()

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
