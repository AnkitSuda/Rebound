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

package com.ankitsuda.rebound.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ankitsuda.rebound.domain.entities.Muscle
import kotlinx.coroutines.flow.Flow

@Dao
interface MusclesDao {

    @Query("SELECT * FROM muscles WHERE tag = :tag")
    fun getMuscle(tag: String): Flow<Muscle>

    @Query("SELECT * FROM muscles")
    fun getMuscles(): Flow<List<Muscle>>

    @Insert
    fun insertMuscles(muscles: List<Muscle>)

}