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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.icons.Weight
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun SessionCompleteQuickInfo(
    modifier: Modifier = Modifier,
    time: String,
    volume: String,
    prs: Int
) {
    Row(modifier = modifier) {
        SessionQuickInfoRowItem(
            icon = Icons.Outlined.Timer,
            text = time,
            contentDescription = "Duration"
        )
        Spacer(modifier = Modifier.width(8.dp))
        SessionQuickInfoRowItem(
            icon = Icons.Filled.Weight,
            text = volume,
            contentDescription = "Total volume"
        )
        Spacer(modifier = Modifier.width(8.dp))
        SessionQuickInfoRowItem(
            icon = Icons.Outlined.EmojiEvents,
            text = "$prs PRs",
            contentDescription = "Total volume"
        )
    }
}

@Composable
fun SessionQuickInfoRowItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    contentDescription: String
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color(117, 117, 117)
        )
        Spacer(Modifier.width(4.dp))
        Text(text = text, style = ReboundTheme.typography.caption, color = Color(117, 117, 117))
    }

}