package com.ankitsuda.rebound

import android.content.Context
import androidx.room.*
import com.ankitsuda.rebound.data.Converters
import com.ankitsuda.rebound.data.daos.MeasurementsDao
import com.ankitsuda.rebound.data.entities.BodyPart
import com.ankitsuda.rebound.data.entities.BodyPartMeasurementLog

@Database(
    entities = [BodyPart::class, BodyPartMeasurementLog::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun measurementsDao(): MeasurementsDao

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