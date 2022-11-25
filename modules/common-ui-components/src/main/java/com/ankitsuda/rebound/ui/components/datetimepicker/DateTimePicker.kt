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

package com.ankitsuda.rebound.ui.components.datetimepicker

import androidx.compose.runtime.*
import java.time.LocalDateTime

@Composable
fun DateTimePicker(
    value: LocalDateTime,
    onCancel: () -> Unit = {},
    onValueChange: (LocalDateTime) -> Unit = {},
) {
    var currentView by remember {
        mutableStateOf(1)
    }

    var pickedDate by remember(value) {
        mutableStateOf(value.toLocalDate())
    }

    var pickedTime by remember(value) {
        mutableStateOf(value.toLocalTime())
    }

    when (currentView) {
        1 -> DatePicker(
            value = pickedDate,
            onCancel = onCancel,
            onValueChange = {
                pickedDate = it
                currentView = 2
            }
        )
        2 -> TimePicker(
            value = pickedTime,
            onCancel = onCancel,
            onValueChange = {
                pickedTime = it
                currentView = 0
                onValueChange(LocalDateTime.of(pickedDate, pickedTime))
            }
        )
    }

}