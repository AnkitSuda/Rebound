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

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import com.ankitsuda.common.compose.R
import com.ankitsuda.rebound.ui.components.AppCard
import com.ankitsuda.rebound.ui.components.SessionCompleteQuickInfo
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HistorySessionItemCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    date: LocalDateTime?,
    totalExercises: Int,
    duration: Long?,
    volume: Double?,
    prs: Int,
) {
    val isSameYear = LocalDate.now().year == date?.year
    val dateFormatter = DateTimeFormatter.ofPattern(if (isSameYear) "EEE, MMM d" else "MMM d, yyyy")


    AppCard(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title, style = ReboundTheme.typography.body1.copy(
                    color = ReboundTheme.colors.onBackground
                )
            )
            if (date != null) {
                Text(
                    text = dateFormatter.format(date), style = ReboundTheme.typography.caption.copy(
                        color = ReboundTheme.colors.onBackground.copy(0.75f)
                    )
                )
            }
            Text(
                text = pluralStringResource(R.plurals.number_of_exercises, totalExercises, totalExercises), style = ReboundTheme.typography.caption.copy(
                    color = ReboundTheme.colors.onBackground.copy(0.75f)
                )
            )
            SessionCompleteQuickInfo(duration = duration, volume = volume, prs = prs)
        }
    }
}