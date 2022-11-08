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
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.rebound.domain.entities.Plate
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import timber.log.Timber

@Composable
fun PlateCalculatorComponent(
    modifier: Modifier,
    weight: Double,
    viewModel: PlateCalculatorComponentViewModel = hiltViewModel()
) {
    val plates by viewModel.plates.collectAsState(emptyList())
    val remainingWeight by viewModel.remainingWeight.collectAsState()

    val theme = LocalThemeState.current

    LaunchedEffect(key1 = weight) {
        viewModel.refreshPlates(weight)
    }

    BoxWithConstraints(modifier = modifier) {
        val comHeight = with(LocalDensity.current) { constraints.minHeight.toDp() }

        Column {
            Column(modifier = Modifier.height(comHeight * 0.35F)) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = "${weight.toReadableString()} kg", // TODO: Move to strings.xml
                    color = theme.keyboardContentColor
                )
                if (remainingWeight > 0.0) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                        text = "Remaining weight: ${remainingWeight.toReadableString()} kg", // TODO: Move to strings.xml
                        style = ReboundTheme.typography.caption,
                        color = theme.keyboardContentColor.copy(alpha = 0.75f)
                    )
                }
            }

            BarbellComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                plates = plates
            )

            RSpacer(space = comHeight * 0.03f)
        }
    }
}

@Composable
private fun BarbellComponent(
    modifier: Modifier,
    plates: List<Plate>
) {
    val theme = LocalThemeState.current

    val barbellHeight = 22.dp
    val barbellColor = theme.keyboardBarbellColor
    val onBarbellColor = theme.keyboardOnBarbellColor

    val barbellWeight = 0F

    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
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
                text = "${barbellWeight.toReadableString()} kg",
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
                .fillMaxHeight()
                .padding(end = 32.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            var platesRowWidth by rememberSaveable {
                mutableStateOf(0F)
            }
            val minimumBarWidth = 150.dp

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