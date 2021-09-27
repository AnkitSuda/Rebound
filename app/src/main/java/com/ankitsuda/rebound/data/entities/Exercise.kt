package com.ankitsuda.rebound.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,


    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "notes")
    var notes: String? = null,

    @ColumnInfo(name = "primary_muscle_id")
    var primaryMuscleId: Long? = null,
    @ColumnInfo(name = "secondary_muscle_id")
    var secondaryMuscleId: Long? = null,
    @ColumnInfo(name = "category_id")
    var categoryId: Long? = null,

    @ColumnInfo(name = "created_at")
    var createdAt: OffsetDateTime? = null,
    @ColumnInfo(name = "update_at")
    var updatedAt: OffsetDateTime? = null,
)