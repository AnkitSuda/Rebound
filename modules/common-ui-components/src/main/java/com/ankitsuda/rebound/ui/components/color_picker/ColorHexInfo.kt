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

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.ankitsuda.base.util.toHexString
import com.ankitsuda.rebound.ui.components.AppTextField
import com.ankitsuda.rebound.ui.components.R

@Composable
fun ColorHexInfo(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    onHexEdited: (Color) -> Unit
) {
    val mSelectedColor by remember(selectedColor) {
        mutableStateOf(selectedColor)
    }
    var text by remember(selectedColor) {
        mutableStateOf("#" + mSelectedColor.toHexString().uppercase()/*.drop(2)*/)
    }

    AppTextField(modifier = modifier, value = text, placeholderValue = stringResource(id = R.string.hex), onValueChange = {
        text = it
        try {
            onHexEdited(Color(android.graphics.Color.parseColor(it)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    })
}