package com.ankitsuda.rebound.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "note")
    var note: String? = null,

    @ColumnInfo(name = "created_at")
    var createdAt: OffsetDateTime? = null,
    @ColumnInfo(name = "update_at")
    var updatedAt: OffsetDateTime? = null,
)
