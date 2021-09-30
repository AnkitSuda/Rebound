package com.ankitsuda.rebound.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ankitsuda.rebound.data.daos.ExercisesDao
import com.ankitsuda.rebound.data.daos.MeasurementsDao
import com.ankitsuda.rebound.data.daos.MusclesDao
import com.ankitsuda.rebound.data.daos.WorkoutsDao
import com.ankitsuda.rebound.data.entities.*
import java.util.concurrent.Executors

@Database(
    entities = [
        BodyPart::class,
        BodyPartMeasurementLog::class,
        Exercise::class,
        ExerciseLog::class,
        ExerciseLogEntry::class,
        ExerciseWorkoutJunction::class,
        Muscle::class,
        Workout::class,
        WorkoutTemplate::class,
        WorkoutTemplateExercise::class,
    ],
    version = 7,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun measurementsDao(): MeasurementsDao
    abstract fun workoutsDao(): WorkoutsDao
    abstract fun musclesDao(): MusclesDao
    abstract fun exercisesDao(): ExercisesDao

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
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        //pre-populate data
                        Executors.newSingleThreadExecutor().execute {
                            instance?.musclesDao()?.insertMuscles(DataGenerator.getMuscles())
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
    }

}