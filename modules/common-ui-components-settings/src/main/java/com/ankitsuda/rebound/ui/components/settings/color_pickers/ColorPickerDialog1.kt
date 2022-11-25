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

package com.ankitsuda.rebound.ui.components.settings.color_pickers

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ankitsuda.common.compose.LocalDialog
import com.ankitsuda.rebound.ui.components.color_picker.ColorPicker1
import com.ankitsuda.rebound.ui.components.color_picker.ColorPickerPresets
import com.ankitsuda.common.compose.R
import com.ankitsuda.rebound.ui.components.color_picker.ColorPicker2


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ColorPickerDialog1(
    defaultColor: Color = Color.Red,
    colorSelected: (Color) -> Unit
) {

    var isPresetLayout by remember {
        mutableStateOf(true)
    }

    var selectedColor by remember {
        mutableStateOf(defaultColor)
    }

//    val screenHeight = (LocalConfiguration.current.screenHeightDp / 1.0).dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .height(screenHeight)
    ) {
        AnimatedContent(targetState = isPresetLayout) {
            if (it) {
                ColorPickerPresets(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
//                    .height(300.dp),
                    onColorPicked = { newColor ->
                        selectedColor = newColor
                    },
                )

            } else {
                    ColorPicker2(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colorSelected = { newColor ->
                            selectedColor = newColor
                        },
                        defaultColor = defaultColor
                    )

            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            TextButton(onClick = {
                isPresetLayout = !isPresetLayout
            }) {
                Text(
                    text = if (isPresetLayout) stringResource(id = R.string.custom) else stringResource(
                        id = R.string.presets
                    )
                )
            }
            TextButton(onClick = with(LocalDialog.current) {
                {
                    hideDialog()
                    colorSelected(selectedColor)
                }
            }) {
                Text(text = stringResource(id = R.string.select))
            }
        }
    }
}