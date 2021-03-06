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

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.color_picker.ColorPicker
import java.text.DecimalFormat

@Composable
fun ColorPickerDialog(onColorSelected: (Color) -> Unit) {

    val defaultColor = MaterialTheme.colors.onSurface

    var selectedColor by remember { mutableStateOf(defaultColor) }
    var alpha by remember { mutableStateOf(1F) }
    var brightness by remember { mutableStateOf(1F) }
    var resetSelectedPosition by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColorPicker(
            alpha = alpha,
            brightness = brightness,
            resetSelectedPosition = resetSelectedPosition,
            onColorSelected = { newSelectedColor ->
                selectedColor = when {
                    newSelectedColor.isSpecified -> newSelectedColor
                    else -> defaultColor
                }
                onColorSelected(selectedColor)
            }
        )

        Column(
        ) {
            val decimalFormatter = DecimalFormat("0.0")

            Text(text = "Alpha: ${decimalFormatter.format(alpha)}")

            Slider(
                value = alpha,
                onValueChange = {
                    alpha = it
                },
                valueRange = 0F..1F,
                steps = 0,
                colors = SliderDefaults.colors(
                    thumbColor = selectedColor,
                    activeTrackColor = selectedColor
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = "Brightness: ${decimalFormatter.format(brightness)}")

            Slider(
                value = brightness,
                onValueChange = {
                    brightness = it
                },
                valueRange = 0F..1F,
                steps = 0,
                colors = SliderDefaults.colors(
                    thumbColor = selectedColor,
                    activeTrackColor = selectedColor
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}