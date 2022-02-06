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

package com.ankitsuda.rebound.ui.settings.personalization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.color_picker.ColorPicker
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@DelicateCoroutinesApi
@Composable
fun ColorPickerDemoScreen() {
    SampleCard {
        val defaultColor = MaterialTheme.colors.onSurface

        var selectedColor by remember { mutableStateOf(defaultColor) }
        var alpha by remember { mutableStateOf(1F) }
        var brightness by remember { mutableStateOf(1F) }
        var resetSelectedPosition by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Controls(
                selectedColor = selectedColor,
                alpha = alpha,
                brightness = brightness,
                defaultColor = defaultColor,
                onSelectedColorChanged = { newSelectedColor ->
                    selectedColor = newSelectedColor
                },
                onAlphaChanged = { newAlpha ->
                    alpha = newAlpha
                },
                onBrightnessChanged = { newBrightness ->
                    brightness = newBrightness
                },
                resetSelectedPosition = {
                    resetSelectedPosition = true

                    GlobalScope.launch {
                        delay(500)
                        resetSelectedPosition = false
                    }
                }
            )

            ColorPicker(
                modifier = Modifier.weight(.66F),
                alpha = alpha,
                brightness = brightness,
                resetSelectedPosition = resetSelectedPosition,
                onColorSelected = { newSelectedColor ->
                    selectedColor = when {
                        newSelectedColor.isSpecified -> newSelectedColor
                        else -> defaultColor
                    }
                }
            )
        }
    }
}

@Composable
private fun ColumnScope.Controls(
    selectedColor: Color,
    defaultColor: Color,
    alpha: Float,
    brightness: Float,
    onSelectedColorChanged: (Color) -> Unit,
    onAlphaChanged: (Float) -> Unit,
    onBrightnessChanged: (Float) -> Unit,
    resetSelectedPosition: () -> Unit
) {
    Row(
        modifier = Modifier.weight(.33F).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pick a color",
                    color = selectedColor
                )

                IconButton(
                    onClick = {
                        onSelectedColorChanged(defaultColor)
                        onAlphaChanged(1F)
                        onBrightnessChanged(1F)
                        resetSelectedPosition()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Back to defaults"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(75.dp)
                    .background(selectedColor)
            )
        }

        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            val decimalFormatter = DecimalFormat("0.0")

            Text(text = "Alpha: ${decimalFormatter.format(alpha)}")

            Slider(
                value = alpha,
                onValueChange = {
                    onAlphaChanged(it)
                },
                valueRange = 0F..1F,
                steps = 0,
                colors = SliderDefaults.colors(thumbColor = selectedColor, activeTrackColor = selectedColor),
                modifier = Modifier.width(200.dp)
            )

            Text(text = "Brightness: ${decimalFormatter.format(brightness)}")

            Slider(
                value = brightness,
                onValueChange = {
                    onBrightnessChanged(it)
                },
                valueRange = 0F..1F,
                steps = 0,
                colors = SliderDefaults.colors(thumbColor = selectedColor, activeTrackColor = selectedColor),
                modifier = Modifier.width(200.dp)
            )
        }
    }
}

@Composable
private fun SampleCard(
    content: @Composable BoxScope.() -> Unit
) {
    MaterialTheme(
        colors = darkColors()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colors.background
                )
        ) {
            Card(
                shape = MaterialTheme.shapes.small.copy(all = CornerSize(8.dp)),
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    content()
                }
            }
        }
    }
}