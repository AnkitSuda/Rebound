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

package com.ankitsuda.rebound.ui.components.workouteditor.supersetselector.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.base.util.colorFromSeed
import com.ankitsuda.base.util.colorFromSupersetId
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.common.compose.R

@Composable
internal fun SupersetExerciseListItem(
    modifier: Modifier = Modifier,
    supersetId: Int?,
    exerciseName: String,
    isSelectedJunction: Boolean,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .run {
                    if (isSelectedJunction) {
                        this
                    } else {
                        clickable(onClick = onClick)
                    }
                }
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            supersetId?.let {
                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .width(4.dp)
                        .background(colorFromSupersetId(supersetId), ReboundTheme.shapes.small)
                )
            }

            Text(
                modifier = Modifier.weight(1f),
                text = exerciseName,
                style = ReboundTheme.typography.subtitle1,
                fontSize = 16.sp,
                color = ReboundTheme.colors.onBackground.copy(alpha = 0.75f)
            )

            if (isSelectedJunction) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    tint = ReboundTheme.colors.primary,
                    contentDescription = stringResource(id = R.string.selected)
                )
            }
        }
    }
}