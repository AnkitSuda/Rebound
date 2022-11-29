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

package com.ankitsuda.rebound.ui.create_exercise.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.AppCard
import com.ankitsuda.rebound.ui.create_exercise.R
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
internal fun ValueSelectorCard(
    modifier: Modifier = Modifier,
    name: String,
    value: String?,
    onClick: () -> Unit,
) {
    val isSelected = value != null

    AppCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = name,
                    style = ReboundTheme.typography.body1,
                    color = ReboundTheme.colors.onBackground
                )
                Text(
                    text = value ?: stringResource(id = R.string.select),
                    style = ReboundTheme.typography.body1,
                    color = if (isSelected) ReboundTheme.colors.onBackground.copy(0.75f) else ReboundTheme.colors.primary
                )
            }

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                tint = ReboundTheme.colors.onBackground.copy(0.75f),
                contentDescription = stringResource(
                    id = R.string.select
                )
            )
        }
    }
}