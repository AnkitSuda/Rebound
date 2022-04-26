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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.ankitsuda.base.util.isDark
import com.ankitsuda.rebound.domain.LogSetType
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SetTypeChangerMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    selectedType: LogSetType,
    onDismissRequest: () -> Unit,
    onChangeSetType: (LogSetType) -> Unit,
) {
    val allTypes = LogSetType.values()

    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(usePlatformDefaultWidth = false)
    ) {
        for (type in allTypes) {
            with(type) {
                val bgColor = if (selectedType == this) {
                    if (MaterialTheme.colors.surface.isDark()) {
                        Color.White
                    } else {
                        Color.Black
                    }.copy(alpha = 0.1f)
                } else {
                    Color.Transparent
                }


                DropdownMenuItem(
                    modifier = Modifier.background(bgColor),
                    onClick = {
                        onChangeSetType(this)
                    }
                ) {
                    SetTypeIcon()
                    SetTypeName()
                }
            }
        }
    }
}

@Composable
private fun LogSetType.SetTypeIcon() {
    val props = getIconProps()
    Box(modifier = Modifier.width(28.dp), contentAlignment = Alignment.CenterStart) {
        Text(text = props.first, color = props.second)
    }
}

@Composable
private fun LogSetType.SetTypeName() {
    Text(text = readableName)
}

fun LogSetType.getIconProps() = when (this) {
    LogSetType.NORMAL -> Pair("N", Color.Gray)
    LogSetType.WARM_UP -> Pair("W", Color.Yellow)
    LogSetType.DROP_SET -> Pair("D", Color.Magenta)
    LogSetType.FAILURE -> Pair("F", Color.Red)
}