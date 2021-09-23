package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ColumnScope.RSpacer(space: Dp) {
    Spacer(
        modifier = Modifier
            .height(space)
    )
}

@Composable
fun RowScope.RSpacer(space: Dp) {
    Spacer(
        modifier = Modifier
            .width(space)
    )
}

