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

import androidx.room.*
import com.ankitsuda.rebound.domain.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementsDao {

    @Query("SELECT * FROM body_parts WHERE id = :partId")
    fun getBodyPartByPartId(partId: String): Flow<BodyPart>

    @Transaction
    @Query("SELECT * FROM body_parts_group")
    fun getBodyPartsWithGroup(): Flow<List<BodyPartWithGroup>>

    @Query("SELECT * FROM body_part_measurement_logs WHERE body_part_id = :partId")
    fun getLogsForPart(partId: String): Flow<List<BodyPartMeasurementLog>>

    @Query("SELECT * FROM body_part_measurement_logs WHERE id = :logId")
    suspend fun getLog(logId: String): BodyPartMeasurementLog

    @Insert
    suspend fun insertMeasurementLog(log: BodyPartMeasurementLog)

    @Insert
    fun insertBodyPartsGroups(groups: List<BodyPartsGroup>)

    @Insert
    fun insertBodyParts(parts: List<BodyPart>)

    @Update
    suspend fun updateMeasurementLog(log: BodyPartMeasurementLog)

    @Query("DELETE FROM body_part_measurement_logs WHERE id = :logId")
    suspend fun deleteMeasurementLogById(logId: String)

}