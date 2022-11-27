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
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.domain.entities.Muscle
import com.ankitsuda.rebound.domain.entities.Barbell
import kotlinx.coroutines.flow.Flow

@Dao
interface BarbellsDao {

    @Query("SELECT * FROM barbells WHERE id = :id")
    fun getBarbell(id: String): Flow<Barbell>

    @Query("SELECT * FROM barbells ORDER BY name")
    fun getBarbells(): Flow<List<Barbell>>

    @Query("SELECT * FROM barbells WHERE is_active = 1 ORDER BY name")
    fun getActiveBarbells(): Flow<List<Barbell>>

    @Query("UPDATE barbells SET is_active = :isActive WHERE id = :barbellId")
    suspend fun updateIsActive(barbellId: String, isActive: Boolean)

    @Insert
    suspend fun insertBarbells(barbells: List<Barbell>)

    @Query("DELETE FROM barbells WHERE id = :barbellId")
    suspend fun deleteBarbell(barbellId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarbell(barbell: Barbell)

}