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

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.ankitsuda.rebound.ui.keyboard.enums.ReboundKeyboardType
import com.ankitsuda.rebound.ui.keyboard.models.ClearNumKey
import com.ankitsuda.rebound.ui.keyboard.models.DecimalNumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumberNumKey
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@Composable
internal fun NumKeysContainerComponent(
    modifier: Modifier = Modifier,
    reboundKeyboardType: ReboundKeyboardType,
    onClickNumKey: (NumKey) -> Unit
) {
    var allKeys by remember(reboundKeyboardType) {
        mutableStateOf(getAllKeys(reboundKeyboardType))
    }

    BoxWithConstraints(modifier = modifier) {
        val keyWidth = with(LocalDensity.current) { constraints.minWidth.toDp() / 3 }
        val keyHeight = with(LocalDensity.current) { constraints.minHeight.toDp() / 4 }

        FlowRow(
            modifier = Modifier,
            mainAxisAlignment = MainAxisAlignment.SpaceEvenly,
            mainAxisSize = SizeMode.Expand,
            content = {
                for (numKey in allKeys) {
                    if (numKey != null) {
                        NumKeyComponent(
                            modifier = Modifier
                                .width(keyWidth)
                                .height(keyHeight),
                            value = numKey,
                            onClick = {
                                onClickNumKey(numKey)
                            }
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .width(keyWidth)
                                .height(keyHeight)
                        )
                    }
                }
            }
        )
    }
}

private fun getAllKeys(
    reboundKeyboardType: ReboundKeyboardType,
): List<NumKey?> {

    val keys = arrayListOf<NumKey?>()

    for (i in 1..9) {
        keys.add(NumberNumKey(value = i))
    }

    if (reboundKeyboardType is ReboundKeyboardType.Weight || reboundKeyboardType is ReboundKeyboardType.Distance) {
        keys.add(DecimalNumKey)
    } else {
        keys.add(null)
    }
    keys.add(NumberNumKey(value = 0))
    keys.add(ClearNumKey)

    return keys
}