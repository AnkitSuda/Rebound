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

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun TemplateItemCard(
    modifier: Modifier = Modifier,
    name: String,
    italicName: Boolean,
    totalExercises: Int,
    onClickPlay: () -> Unit,
    onClick: () -> Unit,
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(
                    text = name, style = MaterialTheme.typography.body1.copy(
                        fontStyle = if (italicName) FontStyle.Italic else FontStyle.Normal
                    ),
                    color = LocalThemeState.current.onBackgroundColor
                )
                Text(
                    color = LocalThemeState.current.onBackgroundColor.copy(alpha = 0.5f),
                    text = "$totalExercises Exercises",
                    style = MaterialTheme.typography.body2,
                )
            }

            IconButton(
                modifier = Modifier.fillMaxHeight(),
                onClick = onClickPlay
            ) {
                Icon(
                    imageVector = Icons.Outlined.PlayArrow,
                    tint = ReboundTheme.colors.primary,
                    contentDescription = "Start workout from template"
                )
            }
        }
    }
}