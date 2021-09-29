package com.ankitsuda.rebound.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ankitsuda.rebound.utils.ExerciseCategory
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Long = 0,


    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "notes")
    var notes: String? = null,

    @ColumnInfo(name = "primary_muscle_tag")
    var primaryMuscleTag: String? = null,
    @ColumnInfo(name = "secondary_muscle_tag")
    var secondaryMuscleTag: String? = null,
    @ColumnInfo(name = "category")
    var category: ExerciseCategory = ExerciseCategory.UNKNOWN,

    @ColumnInfo(name = "created_at")
    var createdAt: OffsetDateTime? = null,
    @ColumnInfo(name = "update_at")
    var updatedAt: OffsetDateTime? = null,
)