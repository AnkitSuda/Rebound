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
import android.view.inputmethod.InputConnection
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.keyboard.models.ClearNumKey
import com.ankitsuda.rebound.ui.keyboard.models.DecimalNumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumberNumKey
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import timber.log.Timber

@Composable
fun ReboundSetKeyboardComponent(
    inputConnection: InputConnection?
) {
    val reboundSetKeyboard = LocalReboundSetKeyboard.current

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

    Box(modifier = Modifier.background(ReboundTheme.colors.background)) {
        NumKeysContainerComponent(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    height = 250.dp,
                ),
            onClickNumKey = {
                Timber.d(it.toString())
                onClickNumKey(it)
            }
        )

    }
}