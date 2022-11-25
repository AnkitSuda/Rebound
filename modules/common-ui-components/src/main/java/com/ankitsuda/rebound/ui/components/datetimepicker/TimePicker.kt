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

import android.app.TimePickerDialog
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime

@Composable
fun TimePicker(
    value: LocalTime,
    is24HourView: Boolean = false,
    onCancel: () -> Unit = {},
    onValueChange: (LocalTime) -> Unit = {},
) {
    val context = LocalContext.current
    val dialog by remember {
        mutableStateOf(
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    onValueChange(LocalTime.of(hour, minute))
                },
                value.hour,
                value.minute,
                is24HourView,
            )
        )
    }

    fun setupDialog() {
        dialog.apply {
            setOnCancelListener {
                onCancel()
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        setupDialog()
        dialog.show()
    }
}