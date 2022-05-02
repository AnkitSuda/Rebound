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

package com.ankitsuda.rebound.ui.keyboard.platecalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.base.util.toRedableString
import com.ankitsuda.rebound.domain.entities.Plate
import com.ankitsuda.rebound.ui.components.RSpacer

@Composable
fun PlateCalculatorComponent(
    modifier: Modifier,
    weight: Float,
    viewModel: PlateCalculatorComponentViewModel = hiltViewModel()
) {
    val plates by viewModel.plates.collectAsState()
//    val plates = viewModel.plates

    LaunchedEffect(key1 = weight) {
        viewModel.refreshPlates(weight)
    }

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "${weight.toRedableString()} kg"
        )
        BarbellComponent(plates = plates)
        RSpacer(space = 16.dp)
    }
}

@Composable
private fun BarbellComponent(plates: List<Plate>) {
    val barbellHeight = 22.dp
    val barbellColor = Color.Gray
    val onBarbellColor = Color.White

    val barbellWeight = 0F

    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(barbellHeight)
                .background(barbellColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${barbellWeight.toRedableString()} kg",
                fontSize = 12.sp,
                color = onBarbellColor
            )
        }


        Box(
            modifier = Modifier
                .width(10.dp)
                .height(barbellHeight + 8.dp)
                .background(barbellColor)
        )

        Box(
            modifier = Modifier
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            var platesRowWidth by rememberSaveable {
                mutableStateOf(0F)
            }
            val minimumBarWidth = 100.dp

            Box(
                modifier = Modifier
                    .width(if (platesRowWidth.dp <= minimumBarWidth) minimumBarWidth else platesRowWidth.dp + 32.dp)
                    .height(barbellHeight)
                    .background(barbellColor)
            )

            PlatesRowComponent(
                plates = plates,
                onChangeWidthDp = {
                    platesRowWidth = it
                }
            )
        }
    }
}

@Composable
private fun PlatesRowComponent(
    onChangeWidthDp: (Float) -> Unit,
    plates: List<Plate>
) {
    var rowHeight by rememberSaveable {
        mutableStateOf(200F)
    }
    val density = LocalDensity.current
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .onGloballyPositioned {
                with(density) {
                    onChangeWidthDp(it.size.width.toDp().value)
                    rowHeight = it.size.height.toFloat()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        for (plate in plates) {
            PlateItemComponent(
                maxHeightF = rowHeight * 0.7F,
                plate = plate,
            )
        }
    }
}