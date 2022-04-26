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

package com.ankitsuda.common.compose

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf

@OptIn(ExperimentalMaterialApi::class)
data class MainPanel constructor(
    val swipeableState: SwipeableState<Int>
) {
    suspend fun expand() {
        swipeableState.animateTo(1)
    }

    suspend fun collapse() {
        swipeableState.animateTo(0)
    }
}

val LocalPanel = staticCompositionLocalOf<MainPanel> {
    error("No LocalPanel given")
}