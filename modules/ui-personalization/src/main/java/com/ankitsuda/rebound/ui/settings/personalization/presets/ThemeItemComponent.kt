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

package com.ankitsuda.rebound.ui.settings.personalization.presets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.domain.entities.ThemePreset
import com.ankitsuda.rebound.ui.components.AppCard
import com.ankitsuda.rebound.ui.components.RButton
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
internal fun ThemeItemComponent(
    modifier: Modifier,
    preset: ThemePreset,
    onClickApply: () -> Unit,
    onClickDelete: () -> Unit
) {

    AppCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            preset.title?.let {
                Text(
                    text = it,
                    style = ReboundTheme.typography.body2,
                    color = ReboundTheme.colors.onBackground
                )
            }
            preset.description?.let {
                RSpacer(space = 4.dp)
                Text(
                    text = it,
                    style = ReboundTheme.typography.caption,
                    color = ReboundTheme.colors.onBackground.copy(alpha = 0.75f)
                )
            }

            RSpacer(space = 16.dp)

            Row(modifier = Modifier.align(Alignment.End)) {
                TextButton(onClick = onClickDelete) {
                    Text(text = "Delete")
                }
                RSpacer(space = 8.dp)
                RButton(onClick = onClickApply) {
                    Icon(imageVector = Icons.Outlined.Check, contentDescription = null)
                    RSpacer(space = 4.dp)
                    Text(text = "Apply")
                }
            }


        }
    }

}