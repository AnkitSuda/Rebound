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

package com.ankitsuda.base.utils

import timber.log.Timber
import java.time.*
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import java.util.stream.IntStream


fun LocalDateTime.toEpochMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
fun Long.toLocalDateTime(): LocalDateTime? =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault());

fun Long.toLocalDate(): LocalDate? = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDate.toEpochMillis() =
    this.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

val LocalDate.yearMonth: YearMonth
    get() = YearMonth.of(year, month)

val YearMonth.next: YearMonth
    get() = this.plusMonths(1)

val YearMonth.previous: YearMonth
    get() = this.minusMonths(1)

fun Long.toDurationStr(): String {
    val totalSeconds = this / 1000
    val seconds = totalSeconds % 60
    val minutes = totalSeconds / 60 % 60
    val hours = totalSeconds / 3600
    val secondsStr = if (seconds > 0 && minutes == 0L && hours == 0L) "${seconds}s" else null
    val minutesStr = if (minutes > 0) "${minutes}m" else null
    val hoursStr = if (hours > 0) "${hours}h" else null
    return listOfNotNull(hoursStr, minutesStr, secondsStr).joinToString(separator = " ")
}

fun LocalDateTime.toReadableDuration(endAt: LocalDateTime = LocalDateTime.now()): String {
    val totalTime =
        endAt.toEpochMillis() - this.toEpochMillis()

    val totalSeconds = totalTime / 1000
    val seconds = totalSeconds % 60
    val minutes = totalSeconds / 60 % 60
    val hours = totalSeconds / 3600
    val readableStr = when {
        hours > 0 -> {
            "$hours hour $minutes min $seconds sec"
        }
        minutes > 0 -> {
            "$minutes min $seconds sec"
        }
        else -> {
            "$seconds sec"
        }
    }

    return readableStr
}


fun LocalDateTime.toReadableDurationStyle2(
    endAt: LocalDateTime = LocalDateTime.now(),
    spaces: Boolean = true
): String {

    val totalTime =
        endAt.toEpochMillis() - this.toEpochMillis()

    val totalSeconds = totalTime / 1000
    val seconds = totalSeconds % 60
    val minutes = totalSeconds / 60 % 60
    val hours = totalSeconds / 3600
    val seprater = if (spaces) " : " else ":"
    // Build formatted String
    return if (hours > 0) "${if (hours < 10) "0" else ""}$hours$seprater" else "" +
            "${if (minutes < 10) "0" else ""}$minutes$seprater" +
            "${if (seconds < 10) "0" else ""}$seconds"
}

fun getCurrentWeekOfMonth(): ArrayList<LocalDate> {
    val today = LocalDate.now();

    val tempList = arrayListOf<LocalDate>()

    val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    val sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    val numOfDaysBetween: Long = ChronoUnit.DAYS.between(monday, sunday)
    val daysBetween = IntStream.iterate(0) { i -> i + 1 }
        .limit(numOfDaysBetween)
        .mapToObj { i -> monday.plusDays(i.toLong()) }
        .collect(Collectors.toList())

    tempList.addAll(daysBetween)
    tempList.add(sunday)

    return tempList
}
