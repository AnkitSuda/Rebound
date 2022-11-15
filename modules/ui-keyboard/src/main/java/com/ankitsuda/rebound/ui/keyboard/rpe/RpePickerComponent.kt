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

package com.ankitsuda.rebound.ui.keyboard.rpe

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.toReadableString

@Composable
internal fun RpePickerComponent(
    modifier: Modifier,
    onSetText: (String) -> Unit,
    text: String?,
    refreshKey: Any?,
) {
    var rpe: Float? by remember(text) {
        mutableStateOf(text?.toFloatOrNull().takeIf { allRPEs.any { r -> r == it } })
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SelectedRpeOverview(
                modifier = Modifier.fillMaxWidth(),
                rpe = rpe,
            )
            RpeSlider(
                modifier = Modifier.fillMaxWidth(),
                value = rpe,
                refreshKey = refreshKey,
                onValueChange = {
                    rpe = it
                    onSetText(rpe?.toReadableString()?:"")
                }
            )
        }
    }
}