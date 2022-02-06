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
import com.ankitsuda.rebound.domain.entities.BodyPartMeasurementLog
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementsDao {

    @Query("SELECT * FROM body_part_measurement_logs WHERE body_part_id = :partId")
    fun getLogsForPart(partId: Long) : Flow<List<BodyPartMeasurementLog>>

    @Query("SELECT * FROM body_part_measurement_logs WHERE id = :logId")
    suspend fun getLog(logId: Long) : BodyPartMeasurementLog

    @Insert
    suspend fun insertMeasurementLog(log: BodyPartMeasurementLog) : Long

    @Update
    suspend fun updateMeasurementLog(log: BodyPartMeasurementLog)

    @Query("DELETE FROM body_part_measurement_logs WHERE id = :logId")
    suspend fun deleteMeasurementLogById(logId: Long)

}