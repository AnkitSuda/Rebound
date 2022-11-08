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

package com.ankitsuda.rebound.ui.workouttemplate.preview.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.workouttemplate.preview.R

@Composable
fun TemplateExerciseComponent(
    modifier: Modifier = Modifier,
    name: String,
    muscle: String,
    onClickInfo: () -> Unit,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = name,
                    style = ReboundTheme.typography.subtitle1,
                    fontSize = 16.sp, color = ReboundTheme.colors.onBackground
                )
                RSpacer(space = 4.dp)
                Text(
                    text = muscle,
                    style = ReboundTheme.typography.caption,
                    fontSize = 14.sp,
                    color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f)
                )
            }
            IconButton(onClick = onClickInfo) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.about_exercise),
                    tint = ReboundTheme.colors.onBackground.copy(alpha = 0.75f)
                )
            }
        }
    }
}