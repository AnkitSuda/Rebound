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

package com.ankitsuda.rebound.ui.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.isDark
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun <A> PopupItemsSettingsItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    selectedItem: A? = null,
    items: List<Pair<A, String>>,
    onItemSelected: (A) -> Unit
) {
    var isMenuExpanded by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier.clickable(onClick = {
        isMenuExpanded = true
    })) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = LocalThemeState.current.onBackgroundColor,
                    modifier = Modifier.padding(start = 2.dp, end = 18.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = text,
                    color = LocalThemeState.current.onBackgroundColor
                )
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = ReboundTheme.typography.caption,
                        color = ReboundTheme.colors.onBackground.copy(alpha = 0.8f),
                    )
                }
            }
            Box {
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false }) {
                    for (item in items) {
                        val bgColor = if (selectedItem == item.first) {
                            if (MaterialTheme.colors.surface.isDark()) {
                                Color.White
                            } else {
                                Color.Black
                            }.copy(alpha = 0.1f)
                        } else {
                            Color.Transparent
                        }

                        DropdownMenuItem(
                            modifier = Modifier.background(bgColor),
                            onClick = {
                                isMenuExpanded = false
                                onItemSelected(item.first)
                            }) {
                            Text(text = item.second)
                        }
                    }
                }
            }
        }
    }
}