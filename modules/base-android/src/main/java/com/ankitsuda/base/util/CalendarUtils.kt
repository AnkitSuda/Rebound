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

package com.ankitsuda.base.util

import java.util.*

// Thanks to https://github.com/CleverPumpkin/CrunchyCalendar

interface CalendarItem
object EmptyItem : CalendarItem
class MonthItem(val date: CalendarDate, val days: List<CalendarItem>) : CalendarItem
class DateItem(val date: CalendarDate) : CalendarItem

class CalendarUtils(private val firstDayOfWeek: Int) {
    companion object {

        const val DAYS_IN_WEEK = 7
        const val MONTHS_PER_PAGE = 6
        const val MONTH_FORMAT = "LLLL yyyy"
        const val DAY_FORMAT = "d"
        const val DATE_VIEW_TYPE = 0
        const val MONTH_VIEW_TYPE = 1
        const val EMPTY_VIEW_TYPE = 2

    }

    /**
     * List of days of week according to [firstDayOfWeek].
     *
     * For example, when [firstDayOfWeek] is [Calendar.MONDAY] list looks like:
     * [MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY]
     */
    private val daysOfWeek = mutableListOf<Int>().apply {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek)

        repeat(DAYS_IN_WEEK) {
            this += calendar.get(Calendar.DAY_OF_WEEK)
            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }
    }

    /**
     * Generate calendar items for months between [dateFrom] and [dateTo]
     */
    fun generateCalendarItems(dateFrom: CalendarDate, dateTo: CalendarDate): List<CalendarItem> {
        val calendar = Calendar.getInstance()
        calendar.time = dateFrom.date

        val calendarItems = mutableListOf<CalendarItem>()
        val monthsBetween = dateFrom.monthsBetween(dateTo)

        repeat(monthsBetween.inc()) {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)

            val itemsForMonth = generateCalendarItemsForMonth(year, month)
            val monthItem = MonthItem(CalendarDate(calendar.time), itemsForMonth)

            calendarItems += monthItem
//            calendarItems += itemsForMonth

            calendar.add(Calendar.MONTH, 1)
        }

        return calendarItems
    }

    fun generateCalendarItemsForMonth(year: Int, month: Int): List<CalendarItem> {
        val itemsForMonth = mutableListOf<CalendarItem>()

        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = firstDayOfWeek

        calendar.set(year, month, 1)
        val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK)
        val startOffset = daysOfWeek.indexOf(firstDayOfMonth)

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        calendar.set(Calendar.DAY_OF_MONTH, daysInMonth)
        val lastDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK)
        val endOffset = DAYS_IN_WEEK.dec() - daysOfWeek.indexOf(lastDayOfMonth)

        // Add empty items for start offset
        repeat(startOffset) { itemsForMonth += EmptyItem }

        calendar.set(year, month, 1)

        // Add date items
        repeat(daysInMonth) {
            val date = CalendarDate(calendar.time)
            itemsForMonth += DateItem(date)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Add empty items for end offset
        repeat(endOffset) { itemsForMonth += EmptyItem }

        return itemsForMonth
    }

}