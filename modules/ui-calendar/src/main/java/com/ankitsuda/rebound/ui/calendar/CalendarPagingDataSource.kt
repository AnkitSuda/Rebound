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

package com.ankitsuda.rebound.ui.calendar

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ankitsuda.rebound.ui.calendar.models.CalendarMonth
import com.ankitsuda.rebound.ui.calendar.models.InDateStyle
import com.ankitsuda.rebound.ui.calendar.models.MonthConfig
import com.ankitsuda.rebound.ui.calendar.models.OutDateStyle
import kotlinx.coroutines.Job
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth

class CalendarPagingDataSource(
    private val startYear: Int,
    private val firstDayOfWeek: DayOfWeek,
) :
    PagingSource<Int, CalendarMonth>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CalendarMonth> {
        val year = params.key ?: startYear
        return try {
            val monthConfig = MonthConfig(
                outDateStyle = OutDateStyle.NONE,
                inDateStyle = InDateStyle.ALL_MONTHS,
                startMonth = YearMonth.of(year, Month.JANUARY),
                endMonth = YearMonth.of(year, Month.DECEMBER),
                hasBoundaries = true,
                maxRowCount = Int.MAX_VALUE,
                firstDayOfWeek = firstDayOfWeek,
                job = Job()
            )


            LoadResult.Page(
                data = monthConfig.months,
                prevKey = year - 1,
                nextKey = year + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CalendarMonth>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}