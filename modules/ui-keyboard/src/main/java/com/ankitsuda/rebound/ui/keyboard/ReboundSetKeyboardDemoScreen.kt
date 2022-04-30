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

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.ankitsuda.rebound.ui.keyboard.field.ReboundSetTextField
import com.ankitsuda.rebound.ui.keyboard.models.NumKey

@Composable
fun ReboundSetKeyboardDemoScreen() {
    var keyboardVisible by remember {
        mutableStateOf(false)
    }
    val liveData = MutableLiveData<NumKey>()
    val reboundSetKeyboard = ReboundSetKeyboard(
        numKeyPressLiveData = liveData,
        onChangeVisibility = {
            keyboardVisible = it
        }
    )

    CompositionLocalProvider(
        LocalReboundSetKeyboard provides reboundSetKeyboard
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            var textValue by remember {
                mutableStateOf("")
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                ReboundSetTextField(
                    modifier = Modifier
                        .width(100.dp)
                        .height(32.dp),
                    value = textValue,
                    onValueChange = {
                        textValue = it
                    }
                )
            }

            Box(modifier = Modifier) {
                if (keyboardVisible) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .align(Alignment.BottomCenter)
                    ) {
                        ReboundSetKeyboardComponent(onClickNumKey = {
                            liveData.postValue(it)
                        })
                    }
                }
            }
        }
    }
}