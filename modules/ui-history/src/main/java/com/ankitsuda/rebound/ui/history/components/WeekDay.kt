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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun WeekDay(
    modifier: Modifier,
    day: LocalDate,
    selectedColor: Color = ReboundTheme.colors.primary,
    defaultColor: Color = ReboundTheme.colors.topBarTitle,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(modifier = modifier.clickable(onClick = onClick)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            val dayDate = day.format(DateTimeFormatter.ofPattern("d"))
            val dayD = day.format(DateTimeFormatter.ofPattern("EEE")).uppercase()

            Text(
                text = dayD,
                style = MaterialTheme.typography.body2,
                color = if (isSelected) selectedColor else defaultColor.copy(alpha = 0.7f)
            )
            Text(
                text = dayDate,
                style = MaterialTheme.typography.body1,
                color = if (isSelected) selectedColor else defaultColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}