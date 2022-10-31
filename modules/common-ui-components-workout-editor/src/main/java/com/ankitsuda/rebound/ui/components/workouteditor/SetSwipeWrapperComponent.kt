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

package com.ankitsuda.rebound.ui.components.workouteditor

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SetSwipeWrapperComponent(
    modifier: Modifier,
    bgColor: Color,
    onSwipeDelete: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val coroutine = rememberCoroutineScope()

    fun handleOnSwiped() {
        coroutine.launch {
            delay(125)
            onSwipeDelete()
        }
    }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it != DismissValue.Default) {
                handleOnSwiped()
                true
            } else {
                false
            }
        }
    )


    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        directions = setOf(
//            DismissDirection.StartToEnd,
            DismissDirection.EndToStart
        ),
        dismissThresholds = {
            FractionalThreshold(0.5f)
        },
        background = {
            SetItemBgLayout(
                bgColor = bgColor,
                dismissState = dismissState
            )
        },
        dismissContent = {
            Timber.d(dismissState.progress.toString());
            Row(
                modifier = Modifier.alpha(
                    1f - (with(dismissState.progress) {
                        if (from == DismissValue.Default && to != DismissValue.Default) {
                            fraction
                        } else {
                            0f
                        }
                    })
                )
            ) {
                content()
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SetItemBgLayout(
    bgColor: Color,
    dismissState: DismissState,
) {
    val direction =
        dismissState.dismissDirection ?: DismissDirection.EndToStart

    Box(
        Modifier
            .fillMaxSize()
            .background(
                if (direction == DismissDirection.EndToStart) ReboundTheme.colors.error.copy(
                    alpha = 0.10f
                ) else bgColor
            )
            .padding(horizontal = 20.dp),
    ) {
        val alignment = Alignment.CenterEnd

        val scale by animateFloatAsState(
            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
        )

        Icon(
            Icons.Outlined.Delete,
            tint = ReboundTheme.colors.error,
            contentDescription = "Delete",
            modifier = Modifier
                .scale(scale)
                .align(alignment)
        )

    }
}