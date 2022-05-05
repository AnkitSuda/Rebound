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

package com.ankitsuda.rebound.ui.keyboard.picker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.numberpicker.ListItemPicker

@Composable
internal fun WarmUpListPickerComponent(
    onSetText: (String) -> Unit,
    startingText: String?
) {
    val allWeights by remember {
        mutableStateOf(getAllWarmUpWeights())
    }
    val allReps by remember {
        mutableStateOf(getAllWarmUpReps())
    }
    var weightState by remember { mutableStateOf(allWeights[0]) }
    var repState by remember { mutableStateOf(allReps[0]) }

    LaunchedEffect(startingText) {
        val arr = startingText?.split(" x ") ?: return@LaunchedEffect
        weightState = allWeights.find { it.second == arr.getOrNull(0) } ?: allWeights[0]
        repState = arr.getOrNull(1)?.toIntOrNull() ?: allReps[0]
    }

    fun setText() {
        onSetText("${weightState.second} x $repState")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ListItemPicker(
            modifier = Modifier.weight(0.4f),
            label = { it.second },
            value = weightState,
            onValueChange = {
                weightState = it
                setText()
            },
            list = allWeights
        )

        Text(
            modifier = Modifier.weight(0.2f),
            text = "X",
            textAlign = TextAlign.Center
        )

        ListItemPicker(
            modifier = Modifier.weight(0.4f),
            label = { it.toString() },
            value = repState,
            onValueChange = {
                repState = it
                setText()
            },
            list = allReps
        )
    }

}

fun getAllWarmUpReps(): List<Int> {
    val list = arrayListOf<Int>()

    for (i in 1..100) {
        list.add(i)
    }

    return list
}

fun getAllWarmUpWeights(): List<Pair<Int, String>> {
    val list = arrayListOf<Pair<Int, String>>()
    list.add(Pair(-1, "Bar"))

    for (i in 5..100) {
        if ((i % 5) == 0) {
            list.add(Pair(i, "$i%"))
        }
    }

    return list
}