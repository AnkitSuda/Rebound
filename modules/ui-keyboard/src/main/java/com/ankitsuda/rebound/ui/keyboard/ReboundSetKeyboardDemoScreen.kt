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

import android.text.InputType
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.keyboard.field.ReboundSetTextField
import com.ankitsuda.rebound.ui.keyboard.models.ClearNumKey
import com.ankitsuda.rebound.ui.keyboard.models.DecimalNumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumberNumKey

@Composable
fun ReboundSetKeyboardDemoScreen() {
    var keyboardVisible by remember {
        mutableStateOf(false)
    }
    var inputConnection: InputConnection? by remember {
        mutableStateOf(null)
    }
    val keyboard = ReboundSetKeyboard(
        onChangeVisibility = {
            keyboardVisible = it
        },
        onChangeInputConnection = {
            inputConnection = it
        }
    )
    CompositionLocalProvider(LocalReboundSetKeyboard provides keyboard) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {


            fun onClickNumKey(numKey: NumKey) {
                if (inputConnection == null) return

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

            var v1 by rememberSaveable {
                mutableStateOf("test")
            }
            var v2 by rememberSaveable {
                mutableStateOf("test2")
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                ReboundSetTextField(
                    contentColor = Color.Black,
                    bgColor = Color.Gray,
                    value = v1, onValueChange = {
                        v1 = it
                    }
                )
                RSpacer(space = 16.dp)
                ReboundSetTextField(
                    contentColor = Color.Black,
                    bgColor = Color.Gray,
                    value = v2, onValueChange = {
                        v2 = it
                    }
                )
            }

            if (keyboardVisible) {

                BackHandler() {
                    keyboardVisible = false
                }

                ReboundSetKeyboardComponent(
                    onClickNumKey = {
                        onClickNumKey(it)
                    }
                )

            }


        }
    }
}