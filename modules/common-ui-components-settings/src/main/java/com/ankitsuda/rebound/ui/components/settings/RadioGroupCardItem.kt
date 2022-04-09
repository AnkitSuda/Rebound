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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.AppCard
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun RadioGroupCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    onSelectionChange: (index: Int, value: String) -> Unit,
    items: List<String>,
    selected: String,
) {
    Box(modifier = modifier) {
        Column() {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = text,
                        color = LocalThemeState.current.onBackgroundColor
                    )
                    if (description.isNotEmpty()) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.caption,
                            color = Color(117, 117, 117)
                        )
                    }
                }

            }
            FlowRow(
                crossAxisSpacing = 8.dp,
                mainAxisSpacing = 8.dp,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                for (item in items) {
                    Row(
                        modifier = Modifier.clickable(onClick = {
                            onSelectionChange(items.indexOf(item), item)
                        }, indication = null,
                            interactionSource = remember { MutableInteractionSource() }),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = item == selected, onClick = {
                            onSelectionChange(items.indexOf(item), item)
                        })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            color = LocalThemeState.current.onBackgroundColor,
                            text = item.replaceFirstChar { it.uppercase() }
                        )
                    }
                }
            }
        }
    }
}