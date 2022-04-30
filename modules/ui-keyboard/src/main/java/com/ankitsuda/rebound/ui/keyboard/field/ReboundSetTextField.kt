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

package com.ankitsuda.rebound.ui.keyboard.field

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.lighterOrDarkerColor
import com.ankitsuda.rebound.ui.keyboard.LocalReboundSetKeyboard
import com.ankitsuda.rebound.ui.keyboard.models.ClearNumKey
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun ReboundSetTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    val keyboard = LocalReboundSetKeyboard.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        keyboard.numKeyPressLiveData.observe(lifecycleOwner) {
            if (it !is ClearNumKey) {
                onValueChange(value + it.toString())
            } else {
                onValueChange(
                    if (value.length <= 1) "" else value.substring(
                        0,
                        value.length - 2
                    )
                )
            }
        }
    }

    Box(
        modifier = modifier
            .background(ReboundTheme.colors.background.lighterOrDarkerColor(0.5f))
            .clickable {
                keyboard.show()
            },
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = value
        )
    }

}