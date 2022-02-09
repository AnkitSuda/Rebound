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


/**
 * Describes the month to which a [CalendarDay] belongs.
 *
 * Thanks to https://github.com/kizitonwose/CalendarView
 */
enum class DayOwner {
    /**
     * Belongs to the previous month on the calendar.
     * Such days are referred to as inDates.
     */
    PREVIOUS_MONTH,

    /**
     * Belongs to the current month on the calendar.
     * Such days are referred to as monthDates.
     */
    THIS_MONTH,

    /**
     * Belongs to the next month on the calendar.
     * Such days are referred to as outDates.
     */
    NEXT_MONTH
}

/**
 * Determines how outDates are generated for each month on the calendar.
 */
enum class OutDateStyle {
    /**
     * The calendar will generate outDates until it reaches
     * the first end of a row. This means that if  a month
     * has 6 rows, it will display 6 rows and if a month
     * has 5 rows, it will display 5 rows.
     */
    END_OF_ROW,

    /**
     * The calendar will generate outDates until
     * it reaches the end of a 6 x 7 grid.
     * This means that all months will have 6 rows.
     */
    END_OF_GRID,

    /**
     * outDates will not be generated.
     */
    NONE
}

/**
 * Determines how inDates are generated for
 * each month on the calendar.
 */
enum class InDateStyle {
    /**
     * inDates will be generated for all months.
     */
    ALL_MONTHS,

    /**
     * inDates will be generated for the first month only.
     */
    FIRST_MONTH,

    /**
     * inDates will not be generated, this means that there
     * will be no offset on any month.
     */
    NONE
}