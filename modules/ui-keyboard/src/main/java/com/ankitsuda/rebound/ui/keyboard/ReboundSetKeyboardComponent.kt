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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.keyboard.enums.KeyboardModeType
import com.ankitsuda.rebound.ui.keyboard.enums.KeyboardType
import com.ankitsuda.rebound.ui.keyboard.models.ClearNumKey
import com.ankitsuda.rebound.ui.keyboard.models.DecimalNumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumberNumKey
import com.ankitsuda.rebound.ui.keyboard.platecalculator.PlateCalculatorComponent
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import timber.log.Timber

@Composable
fun ReboundSetKeyboardComponent(
    keyboardType: KeyboardType,
    inputConnection: InputConnection?
) {
    val reboundSetKeyboard = LocalReboundSetKeyboard.current

    Timber.d("current keyboardType $keyboardType")

    var mCurrentMode by remember {
        mutableStateOf(KeyboardModeType.NUMBERS)
    }

    val currentMode = if (keyboardType == KeyboardType.WEIGHT) {
        mCurrentMode
    } else {
        KeyboardModeType.NUMBERS
    }

    var rightButtonsWidth by remember {
        mutableStateOf(0.dp)
    }

    val density = LocalDensity.current

    BackHandler() {
        reboundSetKeyboard.hide()
    }

    fun onClickNumKey(numKey: NumKey) {
        when (numKey) {
            is NumberNumKey, DecimalNumKey -> inputConnection?.commitText(
                numKey.toString(),
                1
            )
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

    Box() {
        when (currentMode) {
            KeyboardModeType.NUMBERS -> {
                Row(
                    modifier = Modifier
                        .padding(end = rightButtonsWidth)
                        .background(ReboundTheme.colors.background)
                ) {
                    NumKeysContainerComponent(
                        modifier = Modifier
                            .weight(1f)
                            .height(
                                height = 250.dp,
                            ),
                        keyboardType = keyboardType,
                        onClickNumKey = {
                            Timber.d(it.toString())
                            onClickNumKey(it)
                        }
                    )
                }
            }
            KeyboardModeType.PLATE_CALCULATOR -> {
                PlateCalculatorComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            height = 250.dp,
                        ),
                    weight = inputConnection?.getExtractedText(
                        ExtractedTextRequest(),
                        0
                    )?.text.toString().toFloatOrNull() ?: 0f
                )
            }
        }

        Column(
            modifier = Modifier
                .onGloballyPositioned {
                    with(density) {
                        rightButtonsWidth = it.size.width.toDp()
                    }
                }
                .align(Alignment.TopEnd)
        ) {
            if (keyboardType == KeyboardType.WEIGHT) {
                IconButton(onClick = {
                    mCurrentMode = if (currentMode == KeyboardModeType.NUMBERS) {
                        KeyboardModeType.PLATE_CALCULATOR
                    } else {
                        KeyboardModeType.NUMBERS
                    }
                }) {
                    Icon(imageVector = Icons.Outlined.ChangeCircle, contentDescription = null)
                }
            }
        }
    }
}