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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PlateCalculatorComponent(
    modifier: Modifier,
    weight: Float,
    viewModel: PlateCalculatorComponentViewModel = hiltViewModel()
) {
//    val plates by viewModel.plates.collectAsState()
    val plates = viewModel.plates

    val platesScrollState = rememberScrollState()

    LaunchedEffect(key1 = weight) {
        viewModel.refreshPlates(weight)
    }

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "$weight kg"
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = "${plates.size} plates"
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            for (plate in plates) {
                item {
                    PlateItemComponent(plate = plate)
                }
            }
        }
    }

}