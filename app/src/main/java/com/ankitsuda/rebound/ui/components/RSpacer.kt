package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RSpacer(width: Dp = 0.dp, height: Dp = 0.dp) {
    Spacer(modifier = Modifier
        .width(width)
        .height(height))
}

@Composable
fun ColumnScope.AutoSpacer(space: Dp) {
    RSpacer(height = space)
}

@Composable
fun RowScope.AutoSpacer(space: Dp) {
    RSpacer(width = space)
}

