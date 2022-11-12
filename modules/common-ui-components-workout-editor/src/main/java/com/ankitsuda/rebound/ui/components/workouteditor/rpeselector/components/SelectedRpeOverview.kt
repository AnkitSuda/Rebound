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

package com.ankitsuda.rebound.ui.components.workouteditor.rpeselector.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.rebound.ui.components.workouteditor.R
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun SelectedRpeOverview(modifier: Modifier, rpe: Float?) {
    var lastRpe by remember { mutableStateOf(rpe) }
    var mRpe by remember { mutableStateOf(rpe) }

    SideEffect {
        if (mRpe != rpe) {
            lastRpe = mRpe
            mRpe = rpe
        }
    }

    AnimatedContent(
        targetState = mRpe,
        transitionSpec = {
            if ((targetState ?: 0f) > (lastRpe ?: 0f)) {
                // If the target number is larger, it slides up and fades in
                // while the initial (smaller) number slides up and fades out.
                slideInHorizontally { width -> width } + fadeIn() with
                        slideOutHorizontally { width -> -width } + fadeOut()
            } else {
                // If the target number is smaller, it slides down and fades in
                // while the initial number slides down and fades out.
                slideInHorizontally { width -> -width } + fadeIn() with
                        slideOutHorizontally { width -> width } + fadeOut()
            }.using(
                // Disable clipping since the faded slide-in/out should
                // be displayed out of bounds.
                SizeTransform(clip = false)
            )
        },
    ) {

        var title = stringResource(id = R.string.rpe)
        var description: String? = null

        when (it) {
            6f -> {
                title = stringResource(id = R.string.rpe_6_title)
                description = stringResource(id = R.string.rpe_6_description)
            }
            7f -> {
                title = stringResource(id = R.string.rpe_7_title)
                description = stringResource(id = R.string.rpe_7_description)
            }
            7.5f -> {
                title = stringResource(id = R.string.rpe_7_5_title)
                description = stringResource(id = R.string.rpe_7_5_description)
            }
            8f -> {
                title = stringResource(id = R.string.rpe_8_title)
                description = stringResource(id = R.string.rpe_8_description)
            }
            8.5f -> {
                title = stringResource(id = R.string.rpe_8_5_title)
                description = stringResource(id = R.string.rpe_8_5_description)
            }
            9f -> {
                title = stringResource(id = R.string.rpe_9_title)
                description = stringResource(id = R.string.rpe_9_description)
            }
            9.5f -> {
                title = stringResource(id = R.string.rpe_9_5_title)
                description = stringResource(id = R.string.rpe_9_5_description)
            }
            10f -> {
                title = stringResource(id = R.string.rpe_10_title)
                description = stringResource(id = R.string.rpe_10_description)
            }
        }

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (it != null) it.toReadableString() else "?",
                style = ReboundTheme.typography.h3
            )
            Text(
                text = title,
                style = ReboundTheme.typography.subtitle1.copy(color = ReboundTheme.colors.onBackground)
            )

            Text(
                text = description ?: "",
                style = ReboundTheme.typography.subtitle1.copy(
                    color = ReboundTheme.colors.onBackground.copy(alpha = 0.75f)
                )
            )
        }
    }
}