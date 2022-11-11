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

package com.ankitsuda.base.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.luminance
import androidx.core.graphics.ColorUtils
import kotlin.random.Random
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

fun Color.toHexString(): String {
    val alphaString = (alpha * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }
    val redString = (red * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }
    val greenString = (green * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }
    val blueString = (blue * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }

    return "$alphaString$redString$greenString$blueString"
}

fun String.toColor(): Color? =
    try {
        Color(
            android.graphics.Color.parseColor(
                if (this.startsWith("#"))
                    this
                else
                    "#$this"
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

@OptIn(ExperimentalGraphicsApi::class)
fun colorFromSeed(seed: String): Color {
//    val s = 0.65F
//    val l = 0.50F
//
//    var hash = 0
//
//    for (i in seed.indices) {
//        hash = seed.codePointAt(i) + ((hash shl 5) - hash)
//    }
//
//    val h = hash % 360.0F
//
//    return Color.hsl(alpha = 1F, hue = h, saturation = s, lightness = l)

    val rand = Random(seed.hashCode())
    val red: Int = rand.nextInt(256)
    val green: Int = rand.nextInt(256)
    val blue: Int = rand.nextInt(256)

    return Color(red, green, blue)
}

fun colorFromSupersetId(supersetId: Int): Color = colorFromSeed("superset_$supersetId")

