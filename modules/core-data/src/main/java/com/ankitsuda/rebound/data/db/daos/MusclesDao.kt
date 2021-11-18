package com.ankitsuda.rebound.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ankitsuda.rebound.domain.entities.Muscle
import kotlinx.coroutines.flow.Flow

@Dao
interface MusclesDao {

    @Query("SELECT * FROM muscles")
    fun getMuscles(): Flow<List<Muscle>>

    @Insert
    fun insertMuscles(muscles: List<Muscle>)

}