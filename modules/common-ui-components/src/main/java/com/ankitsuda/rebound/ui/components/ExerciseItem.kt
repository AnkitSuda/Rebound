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

package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun ExerciseItem(
    modifier: Modifier = Modifier,
    name: String,
    muscle: String,
    totalLogs: Int,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = name,
                    style = ReboundTheme.typography.subtitle1,
                    fontSize = 16.sp,
                    color = LocalThemeState.current.onBackgroundColor
                )
                RSpacer(space = 4.dp)
                Text(
                    text = muscle,
                    style = ReboundTheme.typography.caption,
                    fontSize = 14.sp,
                    color = LocalThemeState.current.onBackgroundColor.copy(alpha = 0.7f)
                )
            }
            if (totalLogs > 0) {
                Text(
                    text = totalLogs.toString(),
                    style = ReboundTheme.typography.subtitle2,
                    color = LocalThemeState.current.onBackgroundColor.copy(alpha = 0.5f)
                )
            }

        }
    }
}