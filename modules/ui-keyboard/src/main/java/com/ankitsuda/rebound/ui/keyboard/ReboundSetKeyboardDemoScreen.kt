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
import com.ankitsuda.rebound.ui.keyboard.enums.KeyboardType
import com.ankitsuda.rebound.ui.keyboard.field.ReboundSetTextField
import com.ankitsuda.rebound.ui.keyboard.models.ClearNumKey
import com.ankitsuda.rebound.ui.keyboard.models.DecimalNumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumberNumKey
import com.ankitsuda.rebound.ui.keyboard.platecalculator.PlateCalculatorComponent

@Composable
fun ReboundSetKeyboardDemoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        var weight by rememberSaveable {
            mutableStateOf(0.0)
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            ReboundSetTextField(
                contentColor = Color.Black,
                bgColor = Color.Gray,
                keyboardType = KeyboardType.WEIGHT,
                value = weight.toString(), onValueChange = {
                    weight = it.toDoubleOrNull() ?: weight
                }
            )
        }

        PlateCalculatorComponent(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            weight = weight
        )

    }
}