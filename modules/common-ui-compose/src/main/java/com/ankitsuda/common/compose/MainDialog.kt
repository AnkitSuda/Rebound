package com.ankitsuda.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

data class MainDialog(
    var dialogContent: @Composable () -> Unit = {},
    var showDialog: () -> Unit = {},
    var hideDialog: () -> Unit = {}
)

val LocalDialog = compositionLocalOf { MainDialog() }