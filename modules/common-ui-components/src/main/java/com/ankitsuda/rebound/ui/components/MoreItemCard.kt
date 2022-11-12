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
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun MoreItemCard(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    onClick: () -> Unit
) {
    Box(modifier = modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    modifier = Modifier.padding(end = 24.dp),
                    imageVector = icon,
                    contentDescription = text,
                    tint = LocalThemeState.current.onBackgroundColor,
                )
            }
            Column() {
                Text(
                    text = text,
                    style = ReboundTheme.typography.body1,
                    color = ReboundTheme.colors.onBackground
                )
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = ReboundTheme.typography.caption,
                        color = ReboundTheme.colors.onBackground.copy(alpha = 0.8f),
                    )
                }
            }
        }
    }
}