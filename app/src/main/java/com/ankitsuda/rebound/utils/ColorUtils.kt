package com.ankitsuda.rebound.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.core.graphics.ColorUtils
import android.graphics.Color as AndroidColor

fun Color.toLegacyInt(): Int {
    return AndroidColor.argb(
        (alpha * 255.0f + 0.5f).toInt(),
        (red * 255.0f + 0.5f).toInt(),
        (green * 255.0f + 0.5f).toInt(),
        (blue * 255.0f + 0.5f).toInt()
    )
}

fun Color.isDark() = luminance() < 0.5f

fun Color.darkerColor(ratio: Float = 0.5f): Color {
    return Color(ColorUtils.blendARGB(this.toLegacyInt(), AndroidColor.BLACK, ratio))
}

fun Color.lighterColor(ratio: Float = 0.5f): Color {
    return Color(ColorUtils.blendARGB(this.toLegacyInt(), AndroidColor.WHITE, ratio))
}

fun Color.lighterOrDarkerColor(ratio: Float = 0.5f) =
    if (isDark()) lighterColor(ratio) else darkerColor(ratio)