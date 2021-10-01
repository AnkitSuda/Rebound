package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.screens.main_screen.LocalBottomSheet
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import timber.log.Timber

/**
 * Resizes height depends on sheet state
 */
@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BottomSheetStatusBar(color: Color = ReboundTheme.colors.background) {
    val bottomSheetState = LocalBottomSheet.current.state!!
    val statusBarHeight =
        with(LocalDensity.current) { LocalWindowInsets.current.statusBars.top.toDp() }
    val progress = try {
        bottomSheetState.progress
    } catch (e: Exception) {
        null
    }

    val boxHeight = try {
        if (progress != null) {
            when (progress.to) {
                ModalBottomSheetValue.Expanded -> {
                    statusBarHeight * progress.fraction
                }
                ModalBottomSheetValue.HalfExpanded -> {
                    statusBarHeight * (1f - progress.fraction)
                }
                ModalBottomSheetValue.Hidden -> {
                    0.dp
                }
            }
        } else {
            0.dp
        }
    } catch (e: Exception) {
        statusBarHeight
    }

    Box(
        Modifier
            .fillMaxWidth()
            .height(boxHeight)
            .background(color)
    )
}