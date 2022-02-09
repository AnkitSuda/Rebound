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

package com.ankitsuda.rebound.ui.calendar.models


import java.io.Serializable
import java.time.YearMonth

// Thanks to https://github.com/kizitonwose/CalendarView
data class CalendarMonth(
    val yearMonth: YearMonth,
    val weekDays: List<List<CalendarDay>>,
    internal val indexInSameMonth: Int,
    internal val numberOfSameMonth: Int
) : Comparable<CalendarMonth>, Serializable {

    val year: Int = yearMonth.year
    val month: Int = yearMonth.monthValue

    override fun hashCode(): Int {
        return 31 * yearMonth.hashCode() +
                weekDays.first().first().hashCode() +
                weekDays.last().last().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        (other as CalendarMonth)
        return yearMonth == other.yearMonth &&
                weekDays.first().first() == other.weekDays.first().first() &&
                weekDays.last().last() == other.weekDays.last().last()
    }

    override fun compareTo(other: CalendarMonth): Int {
        val monthResult = yearMonth.compareTo(other.yearMonth)
        if (monthResult == 0) { // Same yearMonth
            return indexInSameMonth.compareTo(other.indexInSameMonth)
        }
        return monthResult
    }

    override fun toString(): String {
        return "CalendarMonth { first = ${weekDays.first().first()}, last = ${weekDays.last().last()}} " +
                "indexInSameMonth = $indexInSameMonth, numberOfSameMonth = $numberOfSameMonth"
    }
}