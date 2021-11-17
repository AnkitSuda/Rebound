package com.ankitsuda.rebound.data

import androidx.room.TypeConverter
import com.ankitsuda.base.util.C
import com.ankitsuda.rebound.domain.ExerciseCategory
import com.ankitsuda.rebound.domain.parseToExerciseCategory
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Converters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    // Set timezone value as GMT ... to make time as reasonable
    var df: DateFormat = SimpleDateFormat(C.DB_DATE_FORMAT, Locale.getDefault())


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


    @TypeConverter
    fun toDate(value: String?): Date? {
//        df.timeZone = TimeZone.getTimeZone("GMT")

        return if (value != null) {
            try {
                return df.parse(value)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            null
        } else {
            null
        }
    }

    @TypeConverter
    fun fromDate(value: Date?): String? {
//        df.timeZone = TimeZone.getTimeZone("GMT")

        return if (value != null) {
            df.format(value)
        } else {
            null
        }
    }
}