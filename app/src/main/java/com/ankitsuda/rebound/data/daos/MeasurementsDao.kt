package com.ankitsuda.rebound.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ankitsuda.rebound.data.entities.BodyPartMeasurementLog
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementsDao {

    @Query("SELECT * FROM body_part_measurement_logs WHERE body_part_id = :partId")
    fun getLogsForPart(partId: Long) : Flow<List<BodyPartMeasurementLog>>

    @Insert
    suspend fun insertMeasurementLog(log: BodyPartMeasurementLog) : Long

}