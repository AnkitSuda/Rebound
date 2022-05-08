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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.base.util.lighterOrDarkerColor


@Composable
fun RowScope.SetTextField(
    value: String,
    contentColor: Color,
    bgColor: Color,
    minHeight: Dp = 32.dp,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    ),
    textAlign: TextAlign = TextAlign.Center,
    onValueChange: (String) -> Unit
) {
    var mValue by rememberSaveable {
        mutableStateOf(value)
    }

    fun updateValue(newValue: String) {
        mValue = newValue
        onValueChange(newValue)
    }

    BasicTextField(
        modifier = Modifier
//            .width(64.dp)
            .defaultMinSize(minHeight = minHeight)
            .weight(1.25f)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor.lighterOrDarkerColor(0.10f))
            .padding(start = 8.dp, end = 8.dp),
        textStyle = LocalTextStyle.current.copy(
            textAlign = textAlign,
            fontSize = 14.sp,
            color = contentColor
        ),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = if (textAlign == TextAlign.Center) {
                    Alignment.Center
                } else {
                    Alignment.CenterStart
                }
            ) {
                innerTextField()
            }
        },
        value = mValue,
        onValueChange = {
            updateValue(it)
        },
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
    )
}
