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
import com.ankitsuda.rebound.data.db.daos.*
import com.ankitsuda.rebound.domain.entities.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        BodyPart::class,
        BodyPartsGroup::class,
        BodyPartMeasurementLog::class,
        Exercise::class,
        ExerciseLog::class,
        ExerciseLogEntry::class,
        ExerciseWorkoutJunction::class,
        Muscle::class,
        Workout::class,
        WorkoutTemplate::class,
        Plate::class,
        ThemePreset::class,
        ExerciseSetGroupNote::class,
        WorkoutTemplatesFolder::class,
        Barbell::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun measurementsDao(): MeasurementsDao
    abstract fun workoutsDao(): WorkoutsDao
    abstract fun musclesDao(): MusclesDao
    abstract fun exercisesDao(): ExercisesDao
    abstract fun workoutTemplatesDao(): WorkoutTemplatesDao
    abstract fun platesDao(): PlatesDao
    abstract fun themePresetsDao(): ThemePresetsDao
    abstract fun workoutFoldersFoldersDao(): WorkoutFoldersFoldersDao
    abstract fun barbellsDao(): BarbellsDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        @OptIn(DelicateCoroutinesApi::class)
        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "ReboundDb.db")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        //pre-populate data
                        GlobalScope.launch {
                            instance?.musclesDao()?.insertMuscles(DataGenerator.getMuscles())
                            instance?.measurementsDao()
                                ?.insertBodyPartsGroups(DataGenerator.getBodyPartsGroups())
                            instance?.measurementsDao()
                                ?.insertBodyParts(DataGenerator.getBodyPart())
                            instance?.platesDao()?.insertPlates(DataGenerator.getPlates())
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
    }

}