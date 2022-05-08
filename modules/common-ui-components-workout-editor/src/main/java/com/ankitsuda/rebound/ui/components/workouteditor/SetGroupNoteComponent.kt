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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.domain.entities.ExerciseSetGroupNote
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
internal fun SetGroupNoteComponent(
    modifier: Modifier = Modifier,
    note: ExerciseSetGroupNote,
    onDeleteNote: () -> Unit,
    onChangeValue: (String) -> Unit
) {
    var mNoteText by remember {
        mutableStateOf(note.note ?: "")
    }

    fun handleTextChange(newValue: String) {
        mNoteText = newValue
        onChangeValue(newValue)
    }

    SetSwipeWrapperComponent(
        modifier = modifier,
        bgColor = ReboundTheme.colors.background,
        onSwipeDelete = onDeleteNote
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ReboundTheme.colors.background)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {
            SetTextField(
                value = mNoteText,
                contentColor = ReboundTheme.colors.onBackground,
                bgColor = ReboundTheme.colors.background,
                textAlign = TextAlign.Start,
                minHeight = 48.dp,
                singleLine = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                onValueChange = {
                    handleTextChange(it)
                }
            )
        }
    }
}