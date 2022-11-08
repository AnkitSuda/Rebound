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

package com.ankitsuda.rebound.ui.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.ankitsuda.common.compose.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HistoryHeader(date: LocalDate, totalWorkouts: Int) {
    val isSameYear = LocalDate.now().year == date.year
    val dateFormatter = DateTimeFormatter.ofPattern(if (isSameYear) "MMMM" else "MMMM yyyy")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = dateFormatter.format(date),
            style = ReboundTheme.typography.subtitle1.copy(color = ReboundTheme.colors.onBackground)
        )
        Text(
            text = pluralStringResource(
                id = R.plurals.number_of_workouts,
                totalWorkouts,
                totalWorkouts
            ),
            style = ReboundTheme.typography.subtitle2.copy(
                color = ReboundTheme.colors.onBackground.copy(
                    0.75f
                )
            )
        )
    }
}