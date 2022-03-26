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

package com.ankitsuda.rebound.ui.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ankitsuda.common.compose.LocalDialog
import com.ankitsuda.rebound.ui.components.AppCard
import com.ankitsuda.rebound.ui.components.settings.color_pickers.ColorPickerDialog
import com.ankitsuda.rebound.ui.components.settings.color_pickers.ColorPickerDialog1
import com.ankitsuda.rebound.ui.theme.LocalThemeState
//import com.ankitsuda.rebound.ui.dialogs.ColorPickerAltDialog
//import com.ankitsuda.rebound.ui.dialogs.ColorPickerDialog
//import com.ankitsuda.rebound.ui.dialogs.ColorPickerDialog1
//import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.theme.ShapeValues
//import com.ankitsuda.rebound.utils.lighterOrDarkerColor
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun ColorPickerCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    enableAutoColorPicker: Boolean = true,
    useAltColorPicker: Boolean = true,
    selectedColor: Color = Color.Black,
    onNewColorSelected: (Color) -> Unit = {},
    onClick: () -> Unit = {}
) {
    with(LocalDialog.current) {
        AppCard(modifier = modifier, onClick = {
            onClick()
            if (enableAutoColorPicker) {
                dialogContent = {
                    if (useAltColorPicker) {
                        /* ColorPickerAltDialog(
                             defaultColor = selectedColor,
                             colorSelected = onNewColorSelected
                         )*/
                        ColorPickerDialog1(
                            colorSelected = onNewColorSelected,
                            defaultColor = selectedColor
                        )
                    } else {
                        ColorPickerDialog(onColorSelected = onNewColorSelected)
                    }
                }
                showDialog()
            }
        }) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = text,
                        color = LocalThemeState.current.onBackgroundColor
                    )
                    if (description.isNotEmpty()) {
                        Text(
                            text = description,
                            style = ReboundTheme.typography.caption,
                            color = ReboundTheme.colors.onBackground
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                        .border(
                            width = 1.dp,
                            color = selectedColor/*.lighterOrDarkerColor(ratio = 0.2f)*/,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}


@Composable
fun SwitchCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    checked: Boolean = false,
    onChange: (Boolean) -> Unit = {}
) {
    var mChecked by rememberSaveable {
        mutableStateOf(checked)
    }

    val handleOnChange: (Boolean) -> Unit = {
        mChecked = it
        onChange(it)
    }

    AppCard(modifier = modifier, onClick = {
        handleOnChange(!checked)
    }) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = text,
                    color = LocalThemeState.current.onBackgroundColor
                )
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = ReboundTheme.typography.caption,
                        color = LocalThemeState.current.onBackgroundColor
                    )
                }
            }
            Switch(checked = mChecked, onCheckedChange = handleOnChange)
        }
    }
}

@Composable
fun SliderCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    steps: Int,
    value: Int,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onChange: (Int) -> Unit = {}
) {
    SliderCardItem(
        modifier = modifier,
        icon = icon,
        description = description,
        text = text,
        steps = steps,
        value = value.toFloat(),
        valueRange = valueRange,
        onChange = {
            onChange(it.toInt())
        })
}

@Composable
fun SliderCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    value: Float,
    steps: Int = 1,
    roundValues: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onChange: (Float) -> Unit = {}
) {
    var mValue by remember {
        mutableStateOf(value)
    }

    // Temporary fix for lag
    LaunchedEffect(key1 = value) {
        delay(65)
        if (value != mValue) mValue = value
    }

//    if (value != mValue) mValue = value

    AppCard(modifier = modifier) {

        Row(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {

                Row {
                    Text(
                        text = text,
                        modifier = Modifier.weight(1f),
                        color = LocalThemeState.current.onBackgroundColor
                    )
                    Text(
                        text = mValue.toString(),
                        color = LocalThemeState.current.onBackgroundColor.copy(alpha = 0.7f),
                    )
                }
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = ReboundTheme.typography.caption,
                        color = ReboundTheme.colors.onBackground
                    )
                }
                Slider(
                    value = mValue,
                    onValueChange = {
                        val newValue = if (roundValues) it.roundToInt().toFloat() else it
                        onChange(newValue)
                        mValue = newValue
                    },
                    valueRange = valueRange,
                    steps = steps,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun ShapesEditorCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    defaultValues: ShapeValues = ShapeValues(),
    onValueChange: (ShapeValues) -> Unit
) {
    var isTopLeftExpanded by remember {
        mutableStateOf(false)
    }
    var isTopRightExpanded by remember {
        mutableStateOf(false)
    }
    var isBottomLeftExpanded by remember {
        mutableStateOf(false)
    }
    var isBottomRightExpanded by remember {
        mutableStateOf(false)
    }

    var currentShapeValues by remember {
        mutableStateOf(defaultValues)
    }

    AppCard(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = text,
                        color = LocalThemeState.current.onBackgroundColor
                    )
                    if (description.isNotEmpty()) {
                        Text(
                            text = description,
                            style = ReboundTheme.typography.caption,
                            color = ReboundTheme.colors.onBackground
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Box() {
                    Button(onClick = { isTopLeftExpanded = !isTopLeftExpanded }) {
                        Text(
                            text = currentShapeValues.topStart.toString(),
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier.align(Alignment.TopStart),
                        expanded = isTopLeftExpanded,
                        onDismissRequest = { isTopLeftExpanded = false }) {
                        repeat(100) {
                            DropdownMenuItem(onClick = {
                                isTopLeftExpanded = false
                                currentShapeValues.topStart = it
                                onValueChange(currentShapeValues)
                            }) {
                                Text(
                                    text = it.toString(),
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Button(onClick = { isTopRightExpanded = !isTopRightExpanded }) {
                        Text(text = currentShapeValues.topEnd.toString())
                    }
                    DropdownMenu(
                        expanded = isTopRightExpanded,
                        onDismissRequest = { isTopRightExpanded = false }) {
                        repeat(100) {
                            DropdownMenuItem(onClick = {
                                currentShapeValues = currentShapeValues.copy(topEnd = it)
                                onValueChange(currentShapeValues)
                            }) {
                                Text(text = it.toString())
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier.align(Alignment.BottomStart),
                ) {
                    Button(onClick = { isBottomLeftExpanded = !isBottomLeftExpanded }) {
                        Text(text = currentShapeValues.bottomStart.toString())
                    }
                    DropdownMenu(
                        expanded = isBottomLeftExpanded,
                        onDismissRequest = { isBottomLeftExpanded = false }) {
                        repeat(100) {
                            DropdownMenuItem(onClick = {
                                currentShapeValues = currentShapeValues.copy(bottomStart = it)
                                onValueChange(currentShapeValues)
                            }) {
                                Text(text = it.toString())
                            }
                        }
                    }
                }


                Box(
                    modifier = Modifier.align(Alignment.BottomEnd),
                ) {
                    Button(onClick = { isBottomRightExpanded = !isBottomRightExpanded }) {
                        Text(text = currentShapeValues.bottomEnd.toString())
                    }
                    DropdownMenu(
                        expanded = isBottomRightExpanded,
                        onDismissRequest = { isBottomRightExpanded = false }) {
                        repeat(100) {
                            DropdownMenuItem(onClick = {
                                currentShapeValues = currentShapeValues.copy(bottomEnd = it)
                                onValueChange(currentShapeValues)
                            }) {
                                Text(text = it.toString())
                            }
                        }
                    }
                }

            }
        }
    }
}