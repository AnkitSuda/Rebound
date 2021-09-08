package com.ankitsuda.rebound.ui.components.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ankitsuda.rebound.ui.components.calpose.WEIGHT_7DAY_WEEK
import com.ankitsuda.rebound.ui.components.calpose.model.CalposeDate
import org.threeten.bp.DayOfWeek
import org.threeten.bp.YearMonth

@Composable
fun CalendarWeekRow(
    startDayOffSet: Int,
    endDayCount: Int,
    monthWeekNumber: Int,
    weekCount: Int,
//    priorMonthLength: Int,
    today: CalposeDate,
    month: YearMonth,
) {
    Row {
        if (monthWeekNumber == 0) {
            for (i in 0 until startDayOffSet) {
//                val priorDay = (priorMonthLength - (startDayOffSet - i - 1))
//                widgets.priorMonthDay(
//                    this,
//                    CalposeDate(
//                        priorDay,
//                        month.minusMonths(1).atDay(priorDay).dayOfWeek,
//                        month.minusMonths(1)
//                    )
//                )
                Spacer(modifier = Modifier.weight(WEIGHT_7DAY_WEEK))

            }
        }

        val endDay = when (monthWeekNumber) {
            0 -> 7 - startDayOffSet
            weekCount -> endDayCount
            else -> 7
        }

        for (i in 1..endDay) {
            val day = if (monthWeekNumber == 0) i else (i + (7 * monthWeekNumber) - startDayOffSet)
            val isToday = CalposeDate(day, DayOfWeek.of(i), month) == today

            CalendarDayItem(text = day.toString(), isToday = isToday)

        }

        if (monthWeekNumber == weekCount && endDayCount > 0) {
            for (i in 0 until (7 - endDayCount)) {
                val nextMonthDay = i + 1
                /*widgets.nextMonthDay(
                    this, CalposeDate(
                        nextMonthDay,
                        month.plusMonths(1).atDay(nextMonthDay).dayOfWeek,
                        month.plusMonths(1)
                    )
                )*/
                Spacer(modifier = Modifier.weight(WEIGHT_7DAY_WEEK))
            }
        }
    }
}