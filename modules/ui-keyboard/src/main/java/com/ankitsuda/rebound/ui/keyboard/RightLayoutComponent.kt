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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dialpad
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.ankitsuda.rebound.ui.icons.Plates
import com.ankitsuda.rebound.ui.keyboard.enums.KeyboardModeType
import com.ankitsuda.rebound.ui.keyboard.enums.ReboundKeyboardType
import com.ankitsuda.rebound.ui.theme.LocalThemeState

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun RightLayoutComponent(
    modifier: Modifier,
    currentLayoutMode: KeyboardModeType,
    keyboardType: ReboundKeyboardType,
    onChangeWidth: (Dp) -> Unit,
    onChangeLayoutMode: (KeyboardModeType) -> Unit,
) {
    val theme = LocalThemeState.current

    val density = LocalDensity.current

    Column(
        modifier = modifier
            .onGloballyPositioned {
                with(density) {
                    onChangeWidth(it.size.width.toDp())
                }
            }
    ) {
        if (keyboardType is ReboundKeyboardType.Weight) {
            IconButton(onClick = {
                onChangeLayoutMode(
                    if (currentLayoutMode == KeyboardModeType.NUMBERS) {
                        KeyboardModeType.PLATE_CALCULATOR
                    } else {
                        KeyboardModeType.NUMBERS
                    }
                )
            }) {

                AnimatedContent(targetState = currentLayoutMode) {
                    Icon(
                        imageVector = if (it == KeyboardModeType.NUMBERS) {
                            Icons.Filled.Plates
                        } else {
                            Icons.Outlined.Dialpad
                        }, contentDescription = if (it == KeyboardModeType.NUMBERS) {
                            stringResource(R.string.plate_calculator)
                        } else {
                            stringResource(R.string.keyboard)
                        },
                        tint = theme.keyboardContentColor
                    )
                }
            }
        }
    }

}