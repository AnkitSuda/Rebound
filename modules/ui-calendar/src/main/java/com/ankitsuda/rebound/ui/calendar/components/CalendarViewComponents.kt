/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.ankitsuda.rebound.ui.calendar.components

import android.icu.util.Calendar.WeekData
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.base.util.*
import com.ankitsuda.base.utils.extensions.toArrayList
import com.ankitsuda.base.utils.toLocalDate
import com.ankitsuda.common.compose.LocalAppSettings
import com.ankitsuda.rebound.domain.entities.CountWithDate
import com.ankitsuda.rebound.ui.calendar.models.CalendarDay
import com.ankitsuda.rebound.ui.calendar.models.CalendarMonth
import com.ankitsuda.rebound.ui.calendar.models.DayOwner
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import com.ankitsuda.common.compose.R
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.time.temporal.WeekFields

const val WEIGHT_7DAY_WEEK = 1f / 7f

@Composable
fun CalendarMonthItem(
    month: CalendarMonth,
    countsWithDate: List<CountWithDate>,
    selectedDate: LocalDate,
    onClickOnDay: (CalendarDay) -> Unit
) {
    val firstDayOfWeek = LocalAppSettings.current.firstDayOfWeek

    val dayNames = remember(firstDayOfWeek) {
        val days = (7 - firstDayOfWeek.ordinal)
        var allDays = DayOfWeek.values().toList()
        allDays = allDays.takeLast(days) + allDays.dropLast(days)
        allDays.map { it.getDisplayName(TextStyle.SHORT, Locale.getDefault()) }
    }

//    val dayNames = listOf(
//        stringResource(id = R.string.mon),
//        stringResource(id = R.string.tue),
//        stringResource(id = R.string.wed),
//        stringResource(id = R.string.thu),
//        stringResource(id = R.string.fri),
//        stringResource(id = R.string.sat),
//        stringResource(id = R.string.sun)
//    )

    val monthFormatter = DateTimeFormatter.ofPattern("LLLL yyyy")
    val dayFormatter = DateTimeFormatter.ofPattern("d")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        CalendarMonthHeader(month.yearMonth.format(monthFormatter))

        Row(modifier = Modifier.fillMaxWidth()) {
            for (name in dayNames) {
                CalendarDayNameItem(text = name)
            }
        }

        for (week in month.weekDays) {
            key(week) {
                Row(
                    modifier = Modifier
                        .defaultMinSize(256.dp)
                        .fillMaxWidth()
                ) {
                    for (i in 0..6) {

                        val day = if (i in week.indices) week[i] else null

                        if (day != null && day.owner == DayOwner.THIS_MONTH) {
                            val isToday = day.date == LocalDate.now()
                            val formattedDay = day.date.format(dayFormatter)


                            key("${day.date.dayOfMonth}_${day.date.month}_${day.date.year}") {
                                CalendarDayItem(
                                    text = formattedDay,
                                    modifier = Modifier.weight(
                                        WEIGHT_7DAY_WEEK
                                    ),
                                    isSelected = day.date == selectedDate,
                                    isToday = isToday,
                                    dotVisible = countsWithDate.any { it.date.toLocalDate() == day.date },
                                    onClick = {
                                        onClickOnDay(day)
                                    }
                                )
                            }

                        } else {
                            Spacer(modifier = Modifier.weight(WEIGHT_7DAY_WEEK))
                        }
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
        color = LocalThemeState.current.onBackgroundColor,
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
        style = ReboundTheme.typography.body2.copy(color = ReboundTheme.colors.onBackground),
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



