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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ankitsuda.rebound.ui.keyboard.enums.ReboundKeyboardType
import com.ankitsuda.rebound.ui.keyboard.field.ReboundSetTextField
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
                reboundKeyboardType = ReboundKeyboardType.WEIGHT,
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