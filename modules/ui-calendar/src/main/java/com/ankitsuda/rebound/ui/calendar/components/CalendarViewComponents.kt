package com.ankitsuda.rebound.ui.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.base.util.*
import java.text.SimpleDateFormat
import java.util.*

const val WEIGHT_7DAY_WEEK = 1f / 7f

@Composable
fun CalendarMonthItem(
    month: MonthItem,
    days: List<CalendarItem>,
    selectedDate: CalendarDate,
    onClickOnDay: (DateItem) -> Unit
) {
    val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val weeks = days.chunked(7)
    val monthFormatter = SimpleDateFormat(CalendarUtils.MONTH_FORMAT, Locale.getDefault())
    val dayFormatter = SimpleDateFormat(CalendarUtils.DAY_FORMAT, Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {

        CalendarMonthHeader(monthFormatter.format(month.date.date))

        Row(modifier = Modifier.fillMaxWidth()) {
            for (name in dayNames) {
                CalendarDayNameItem(text = name)
            }
        }




        for (week in weeks) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (day in week) {
                    if (day is DateItem) {
                        val isToday = day.date == CalendarDate.today
                        val formattedDay = dayFormatter.format(day.date.date)


                        CalendarDayItem(
                            text = formattedDay,
                            modifier = Modifier.weight(
                                WEIGHT_7DAY_WEEK
                            ),
                            isSelected = day.date == selectedDate,
                            isToday = isToday,
                            dotVisible = false,
                            onClick = {
                                onClickOnDay(day)
                            }
                        )

                    } else {
                        Spacer(modifier = Modifier.weight(WEIGHT_7DAY_WEEK))
                    }
                }
            }
        }
    }

}

@Composable
fun ColumnScope.CalendarMonthHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .align(Alignment.CenterHorizontally)
    )
}

@Composable
fun RowScope.CalendarDayNameItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 16.dp)
            .weight(WEIGHT_7DAY_WEEK)
            .align(Alignment.CenterVertically)
    )
}

@Composable
fun CalendarDayItem(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean = false,
    isToday: Boolean = false,
    dotVisible: Boolean = false,
    onClick: () -> Unit
) {
    val textColor =
        if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface




    Box(
        modifier = modifier
            .clip(RoundedCornerShape(25))
            .clickable(onClick = onClick),
    ) {
        if (isToday || isSelected) {

            var bgModifier = Modifier
                .align(Alignment.Center)
                .size(28.dp)
                .clip(CircleShape)

            if (isToday) {
                bgModifier = bgModifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                    shape = CircleShape
                )
            }
            if (isSelected) {
                bgModifier = bgModifier.background(MaterialTheme.colors.primary)
            }

            Box(modifier = bgModifier)
        }
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 16.dp)
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            color = textColor,
            style = MaterialTheme.typography.body1
        )

        if (dotVisible) {
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
                    .size(2.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
            )
        }
    }
}



