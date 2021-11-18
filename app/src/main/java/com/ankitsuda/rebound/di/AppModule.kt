package com.ankitsuda.rebound.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.ankitsuda.base.initializers.AppInitializers
import com.ankitsuda.base.initializers.TimberInitializer
import com.ankitsuda.base.utils.CoroutineDispatchers
import com.ankitsuda.rebound.data.db.AppDatabase
import com.ankitsuda.rebound.data.db.daos.ExercisesDao
import com.ankitsuda.rebound.data.db.daos.MeasurementsDao
import com.ankitsuda.rebound.data.db.daos.MusclesDao
import com.ankitsuda.rebound.data.db.daos.WorkoutsDao
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


}
