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

package com.ankitsuda.rebound.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.theme.LocalThemeState

@Composable
fun HomeScreen() {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = LocalThemeState.current.backgroundColor
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopBar(title = "Home")
        }
    }


}