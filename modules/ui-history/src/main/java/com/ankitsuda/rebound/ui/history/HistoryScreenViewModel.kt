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
import com.ankitsuda.base.util.CalendarDate
import com.ankitsuda.rebound.domain.entities.Workout
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {
    private var _week: SnapshotStateList<Date> = mutableStateListOf()
    val week = _week

    val today = CalendarDate.today.date

    fun getCurrentWeek() {
        val c = Calendar.getInstance().apply {
            this.set(Calendar.HOUR_OF_DAY, 0)
            this.set(Calendar.MINUTE, 0)
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
        }
        val tempList = arrayListOf<Date>()
        c[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        for (i in 0..6) {
            tempList.add(c.time)
            c.add(Calendar.DATE, 1)
        }
//        tempList.removeAt(0)
        week.clear()
        week.addAll(tempList)
    }

    fun getWorkoutsOnDate(date: Date): Flow<List<Workout>> =
        workoutsRepository.getAllWorkoutsOnDate(date)

}