package com.ankitsuda.rebound.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

data class ShapeValues(
    var topStart: Int = 0,
    var bottomStart: Int = 0,
    var topEnd: Int = 0,
    var bottomEnd: Int = 0,
)

internal val LocalReboundShapes = staticCompositionLocalOf { Shapes() }
