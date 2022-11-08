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

package com.ankitsuda.rebound.ui.workout_panel.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.workout_panel.R
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode


@Composable
fun WorkoutQuickInfo(
    currentDurationStr: String,
    currentVolumeStr: String,
    currentSetsStr: String
) {

    FlowRow(
        mainAxisAlignment = MainAxisAlignment.SpaceEvenly,
        mainAxisSize = SizeMode.Expand,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
    ) {
        InfoItem(value = currentDurationStr, title = stringResource(R.string.duration))
        InfoItem(value = currentVolumeStr, title = stringResource(R.string.volume))
        InfoItem(value = currentSetsStr, title = stringResource(R.string.sets))
    }
}

@Composable
private fun InfoItem(value: String, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            textAlign = TextAlign.Center,
            style = ReboundTheme.typography.body1,
            color = ReboundTheme.colors.onBackground
        )
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = ReboundTheme.typography.body2,
            color = ReboundTheme.colors.onBackground.copy(alpha = 0.75f),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}