package com.ankitsuda.rebound.data

import androidx.room.TypeConverter
import com.ankitsuda.rebound.utils.ExerciseCategory
import com.ankitsuda.rebound.utils.parseToExerciseCategory
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

object Converters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    @JvmStatic
    fun toExerciseCategory(value: String): ExerciseCategory {
        return value.parseToExerciseCategory()

    }

    @TypeConverter
    @JvmStatic
    fun fromExerciseCategory(category: ExerciseCategory?): String {
        return category?.tag ?: ExerciseCategory.UNKNOWN.tag
    }

}