package com.ankitsuda.rebound.data.daos

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