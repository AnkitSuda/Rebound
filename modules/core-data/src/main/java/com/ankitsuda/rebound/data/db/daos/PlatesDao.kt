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
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ankitsuda.rebound.domain.entities.Muscle
import com.ankitsuda.rebound.domain.entities.Plate
import kotlinx.coroutines.flow.Flow

@Dao
interface PlatesDao {

    @Query("SELECT * FROM plates WHERE id = :id")
    fun getPlate(id: String): Flow<Plate>

    @Query("SELECT * FROM plates ORDER BY weight")
    fun getPlates(): Flow<List<Plate>>

    @Query("SELECT * FROM plates WHERE is_active = 1")
    fun getActivePlates(): Flow<List<Plate>>

    @Query("UPDATE plates SET is_active = :isActive WHERE id = :plateId")
    suspend fun updateIsActive(plateId: String, isActive: Boolean)

    @Insert
    suspend fun insertPlates(plates: List<Plate>)

    @Query("DELETE FROM plates WHERE id = :plateId")
    suspend fun deletePlate(plateId: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlate(plate: Plate)
}