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

package com.ankitsuda.rebound.ui.components.workouteditor.durationeditor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ankitsuda.base.utils.toString
import com.ankitsuda.rebound.ui.components.RButtonStyle2
import com.ankitsuda.rebound.ui.components.datetimepicker.DateTimePicker
import com.ankitsuda.rebound.ui.components.workouteditor.R
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun WorkoutDurationEditorDialog(
    initialStartDateTime: LocalDateTime,
    initialEndDateTime: LocalDateTime,
    onDismissRequest: () -> Unit,
    onSave: (startDateTime: LocalDateTime, endDateTime: LocalDateTime) -> Unit,
) {
    var currentPickerActiveFor by remember {
        mutableStateOf(0)
    }

    var startDateTime by rememberSaveable {
        mutableStateOf(initialStartDateTime)
    }

    var endDateTime by rememberSaveable {
        mutableStateOf(initialEndDateTime)
    }

    fun getFormattedDateString(dateTime: LocalDateTime): String =
        dateTime.format(
            DateTimeFormatter.ofLocalizedDateTime(
                FormatStyle.MEDIUM,
                FormatStyle.SHORT
            )
        )

    fun handleSaveClick() {
        onSave(
            startDateTime,
            endDateTime
        )
        onDismissRequest()
    }

    val formattedStartDateTime = remember(startDateTime) {
        getFormattedDateString(startDateTime)
    }

    val formattedEndDateTime = remember(endDateTime) {
        getFormattedDateString(endDateTime)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            color = ReboundTheme.colors.background,
            shape = ReboundTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier =
                    Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.adjust_duration),
                        style = ReboundTheme.typography.h6,
                        color = ReboundTheme.colors.onBackground
                    )

                    Text(
                        text = stringResource(R.string.start_time),
                        style = ReboundTheme.typography.caption,
                        color = ReboundTheme.colors.onBackground.copy(0.75f)
                    )

                    RButtonStyle2(
                        modifier = Modifier.fillMaxWidth(),
                        text = formattedStartDateTime,
                        onClick = {
                            currentPickerActiveFor = 1
                        }
                    )

                    Text(
                        text = stringResource(R.string.end_time),
                        style = ReboundTheme.typography.caption,
                        color = ReboundTheme.colors.onBackground.copy(0.75f)
                    )

                    RButtonStyle2(
                        modifier = Modifier.fillMaxWidth(),
                        text = formattedEndDateTime,
                        onClick = {
                            currentPickerActiveFor = 2
                        }
                    )

                    DialogButtonsRow(
                        onClickSave = ::handleSaveClick,
                        onClickCancel = onDismissRequest
                    )
                }
            }
        }
    }

    if (currentPickerActiveFor == 1 || currentPickerActiveFor == 2) {
        DateTimePicker(
            value = when (currentPickerActiveFor) {
                2 -> endDateTime
                else -> startDateTime
            },
            onCancel = {
                currentPickerActiveFor = 0
            },
            onValueChange = {
                when (currentPickerActiveFor) {
                    1 -> startDateTime = it
                    2 -> endDateTime = it
                }
                currentPickerActiveFor = 0
            }
        )
    }
}

@Composable
private fun ColumnScope.DialogButtonsRow(
    onClickSave: () -> Unit,
    onClickCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .align(Alignment.End),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextButton(onClick = onClickCancel) {
            Text(stringResource(id = R.string.cancel))
        }
        TextButton(onClick = onClickSave) {
            Text(stringResource(id = R.string.save))
        }
    }
}