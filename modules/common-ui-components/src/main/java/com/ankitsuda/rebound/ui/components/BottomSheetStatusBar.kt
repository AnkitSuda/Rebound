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

package com.ankitsuda.rebound.ui.components

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
//import com.ankitsuda.rebound.ui.screens.main_screen.LocalBottomSheet
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

/**
 * Resizes height depends on sheet state
 */
@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BottomSheetStatusBar(color: Color = ReboundTheme.colors.background) {
//    val bottomSheetState = LocalBottomSheet.current.state!!
//    val statusBarHeight =
//        with(LocalDensity.current) { LocalWindowInsets.current.statusBars.top.toDp() }
//    val progress = try {
//        bottomSheetState.progress
//    } catch (e: Exception) {
//        null
//    }
//
//    val boxHeight = try {
//        if (progress != null) {
//            when (progress.to) {
//                ModalBottomSheetValue.Expanded -> {
//                    statusBarHeight * progress.fraction
//                }
//                ModalBottomSheetValue.HalfExpanded -> {
//                    statusBarHeight * (1f - progress.fraction)
//                }
//                ModalBottomSheetValue.Hidden -> {
//                    0.dp
//                }
//            }
//        } else {
//            0.dp
//        }
//    } catch (e: Exception) {
//        statusBarHeight
//    }
//
//    Box(
//        Modifier
//            .fillMaxWidth()
//            .height(boxHeight)
//            .background(color)
//    )
}