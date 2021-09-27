package com.ankitsuda.rebound

import android.content.Context
import androidx.room.*
import com.ankitsuda.rebound.data.Converters
import com.ankitsuda.rebound.data.daos.MeasurementsDao
import com.ankitsuda.rebound.data.daos.WorkoutsDao
import com.ankitsuda.rebound.data.entities.*

@Database(
    entities = [
        BodyPart::class,
        BodyPartMeasurementLog::class,
        Exercise::class,
        ExerciseLog::class,
        ExerciseLogEntry::class,
        ExerciseWorkoutJunctions::class,
        Muslce::class,
        Workout::class,
        WorkoutTemplate::class,
        WorkoutTemplateExercise::class,
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun measurementsDao(): MeasurementsDao
    abstract fun workoutsDao(): WorkoutsDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "ReboundDb.db")
                .fallbackToDestructiveMigration()
                .build()
    }

}