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

package com.ankitsuda.rebound.ui.history

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SUNDAY
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters.nextOrSame
import java.time.temporal.TemporalAdjusters.previousOrSame
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {
    private var _week: SnapshotStateList<LocalDate> = mutableStateListOf()
    val week = _week

    fun getCurrentWeek() {
        val today = LocalDate.now();

        val tempList = arrayListOf<LocalDate>()

        val monday = today.with(previousOrSame(MONDAY));
        val sunday = today.with(nextOrSame(SUNDAY));

        val numOfDaysBetween: Long = ChronoUnit.DAYS.between(monday, sunday)
        val daysBetween = IntStream.iterate(0) { i -> i + 1 }
            .limit(numOfDaysBetween)
            .mapToObj { i -> monday.plusDays(i.toLong()) }
            .collect(Collectors.toList())

        tempList.addAll(daysBetween)
        tempList.add(sunday)
        week.clear()
        week.addAll(tempList)

        Timber.d("Week days $tempList")
    }

    fun getWorkoutsOnDate(date: LocalDate): Flow<List<Workout>> =
        workoutsRepository.getAllWorkoutsOnDate(date)

}