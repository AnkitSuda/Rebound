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

package com.ankitsuda.rebound.ui.keyboard

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.keyboard.models.ClearNumKey
import com.ankitsuda.rebound.ui.keyboard.models.DecimalNumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumberNumKey
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
internal fun NumKeyComponent(
    modifier: Modifier = Modifier,
    value: NumKey,
    onClick: () -> Unit
) {
    val theme = LocalThemeState.current

    val contentColor = theme.keyboardContentColor

    Box(
        modifier = modifier
            .clip(ReboundTheme.shapes.medium)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when (value) {
            is NumberNumKey -> Text(
                modifier = Modifier,
                text = value.value.toString(),
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            is DecimalNumKey -> Text(
                modifier = Modifier,
                text = ".",
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            is ClearNumKey -> Icon(
                modifier = Modifier,
                imageVector = Icons.Outlined.Backspace,
                contentDescription = stringResource(id = R.string.clear),
                tint = contentColor
            )
        }
    }
}