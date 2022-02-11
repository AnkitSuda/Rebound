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

package com.ankitsuda.rebound.data.repositories

import com.ankitsuda.base.utils.generateId
import com.ankitsuda.rebound.data.db.daos.MeasurementsDao
import com.ankitsuda.rebound.domain.entities.BodyPartMeasurementLog
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class MeasurementsRepository @Inject constructor(
    private val measurementsDao: MeasurementsDao
) {

    fun getLogsForPart(partId: String) = measurementsDao.getLogsForPart(partId)

    suspend fun getLog(logId: String) = measurementsDao.getLog(logId)

    /**
     * Testing just for now
     */
    suspend fun addMeasurementToDb(measurement: Float, partId: String) {
        measurementsDao.insertMeasurementLog(
            BodyPartMeasurementLog(
                id = generateId(),
                bodyPartId = partId,
                measurement = measurement,
                createdAt = LocalDateTime.now()
            )
        )
    }


    suspend fun updateMeasurement(partMeasurementLog: BodyPartMeasurementLog) {
        measurementsDao.updateMeasurementLog(partMeasurementLog)
    }

    suspend fun deleteMeasurementFromDb(measurementId: String) {
        measurementsDao.deleteMeasurementLogById(measurementId)
    }
}