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

package com.ankitsuda.rebound.ui.components.color_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor

@Composable
fun ColorPicker2(
    modifier: Modifier = Modifier,
    defaultColor: Color = Color.Red,
    colorSelected: (Color) -> Unit
) {
    var mColorFromHex by remember(key1 = defaultColor) {
        mutableStateOf(defaultColor)
    }

    var mColor by remember(key1 = defaultColor) {
        mutableStateOf(defaultColor)
    }

    LaunchedEffect(key1 = mColorFromHex, block = {
        mColor = mColorFromHex
    })

    var colorHexInfoHeight by remember {
        mutableStateOf(30.dp)
    }

    BoxWithConstraints(
        modifier = modifier
    ) {
        val minWidth = this.minWidth
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            key(mColorFromHex) {
                ClassicColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(minWidth),
                    color = mColorFromHex,
                    onColorChanged = { color: HsvColor ->
                        mColor = color.toColor()
                        colorSelected(color.toColor())
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .height(colorHexInfoHeight)
                        .width(colorHexInfoHeight)
                        .background(mColor)
                )
                Spacer(modifier = Modifier.width(16.dp))
                ColorHexInfo(
                    modifier = with(LocalDensity.current) {
                        Modifier.onGloballyPositioned {
                            colorHexInfoHeight = it.size.height.toDp()
                        }
                    },
                    selectedColor = mColor,
                    onHexEdited = {
                        mColorFromHex = it
                        colorSelected(it)
                    }
                )
            }
        }
    }
}