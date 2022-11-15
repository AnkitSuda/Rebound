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

package com.ankitsuda.rebound.ui.keyboard

import android.text.TextUtils
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.common.compose.LocalAppSettings
import com.ankitsuda.rebound.ui.keyboard.enums.KeyboardModeType
import com.ankitsuda.rebound.ui.keyboard.enums.ReboundKeyboardType
import com.ankitsuda.rebound.ui.keyboard.models.ClearNumKey
import com.ankitsuda.rebound.ui.keyboard.models.DecimalNumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumberNumKey
import com.ankitsuda.rebound.ui.keyboard.picker.WarmUpListPickerComponent
import com.ankitsuda.rebound.ui.keyboard.platecalculator.PlateCalculatorComponent
import com.ankitsuda.rebound.ui.theme.LocalThemeState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ReboundSetKeyboardComponent(
    reboundKeyboardType: ReboundKeyboardType,
    inputConnection: InputConnection?,
    onHideKeyboard: () -> Unit
) {
    val theme = LocalThemeState.current

    var mCurrentMode by remember {
        mutableStateOf(KeyboardModeType.NUMBERS)
    }

    val currentMode = when (reboundKeyboardType) {
        ReboundKeyboardType.WEIGHT -> mCurrentMode
        ReboundKeyboardType.WARMUP_SET -> KeyboardModeType.WARMUP_PICKER
        else -> KeyboardModeType.NUMBERS
    }

    var rightButtonsWidth by remember {
        mutableStateOf(0.dp)
    }

    BackHandler {
        onHideKeyboard()
    }

    fun onClickNumKey(numKey: NumKey) {
        handleOnClickNumKey(
            reboundKeyboardType = reboundKeyboardType,
            numKey = numKey,
            inputConnection = inputConnection
        )
    }

    Box {
        AnimatedContent(targetState = currentMode) { mCurrentMode ->
            when (mCurrentMode) {
                KeyboardModeType.NUMBERS -> {
                    NumKeysContainerComponent(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(
                                height = 250.dp,
                            )
                            .padding(end = rightButtonsWidth)
                            .background(theme.keyboardBackgroundColor),
                        reboundKeyboardType = reboundKeyboardType,
                        onClickNumKey = { numKey ->
                            onClickNumKey(numKey)
                        }
                    )
                }
                KeyboardModeType.PLATE_CALCULATOR -> {
                    key(LocalAppSettings.current.weightUnit) {
                        PlateCalculatorComponent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(
                                    height = 250.dp,
                                ),
                            weight = inputConnection?.getText()?.toDoubleOrNull() ?: 0.0
                        )
                    }
                }
                KeyboardModeType.WARMUP_PICKER -> {
                    WarmUpListPickerComponent(
                        onSetText = {
                            val currentText = inputConnection?.getText() ?: ""
                            val beforeCursorText =
                                inputConnection?.getTextBeforeCursor(currentText.length, 0) ?: ""
                            val afterCursorText =
                                inputConnection?.getTextAfterCursor(currentText.length, 0) ?: ""
                            inputConnection?.deleteSurroundingText(
                                beforeCursorText.length,
                                afterCursorText.length
                            )
                            inputConnection?.commitText(
                                it,
                                1
                            )
                        },
                        startingText = inputConnection?.getText()
                    )
                }
            }
        }

        RightLayoutComponent(
            modifier = Modifier
                .align(Alignment.TopEnd),
            currentLayoutMode = currentMode,
            keyboardType = reboundKeyboardType,
            onChangeLayoutMode = {
                mCurrentMode = it
            },
            onChangeWidth = {
                rightButtonsWidth = it
            }
        )
    }
}

private fun handleOnClickNumKey(
    reboundKeyboardType: ReboundKeyboardType,
    numKey: NumKey,
    inputConnection: InputConnection?
) {
    val currentText = inputConnection?.getText() ?: ""

    fun commitText() {
        inputConnection?.commitText(
            numKey.toString(),
            1
        )
    }

    when (numKey) {
        is NumberNumKey -> commitText()
        is DecimalNumKey -> if ((reboundKeyboardType == ReboundKeyboardType.WEIGHT || reboundKeyboardType == ReboundKeyboardType.DISTANCE) && !currentText.contains(
                "."
            )
        ) {
            commitText()
        }
        is ClearNumKey -> {
            val selectedText = inputConnection?.getSelectedText(0)

            if (TextUtils.isEmpty(selectedText)) {
                inputConnection?.deleteSurroundingText(1, 0)
            } else {
                inputConnection?.commitText("", 1)
            }
        }
    }
}

fun InputConnection.getText() = getExtractedText(
    ExtractedTextRequest(),
    0
)?.text.toString()
