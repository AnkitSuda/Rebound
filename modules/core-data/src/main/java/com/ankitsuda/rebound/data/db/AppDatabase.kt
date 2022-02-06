/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.ankitsuda.rebound.data.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ankitsuda.rebound.data.db.daos.ExercisesDao
import com.ankitsuda.rebound.data.db.daos.MeasurementsDao
import com.ankitsuda.rebound.data.db.daos.MusclesDao
import com.ankitsuda.rebound.data.db.daos.WorkoutsDao
import com.ankitsuda.rebound.domain.entities.*
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