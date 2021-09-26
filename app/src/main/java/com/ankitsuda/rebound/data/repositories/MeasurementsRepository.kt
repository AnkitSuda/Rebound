package com.ankitsuda.rebound.data.repositories

import com.ankitsuda.rebound.data.daos.MeasurementsDao
import com.ankitsuda.rebound.data.entities.BodyPartMeasurementLog
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class MeasurementsRepository @Inject constructor(
    private val measurementsDao: MeasurementsDao
) {

    fun getLogsForPart(partId: Long) = measurementsDao.getLogsForPart(partId)

    suspend fun getLog(logId: Long) = measurementsDao.getLog(logId)

    /**
     * Testing just for now
     */
    suspend fun addMeasurementToDb(measurement: Float, partId: Long) {
        measurementsDao.insertMeasurementLog(
            BodyPartMeasurementLog(
                bodyPartId = partId,
                measurement = measurement,
                createdAt = OffsetDateTime.now()
            )
        )
    }


    suspend fun deleteMeasurementFromDb(measurementId: Long) {
        measurementsDao.deleteMeasurementLogById(measurementId)
    }
}