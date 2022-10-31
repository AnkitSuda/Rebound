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

package com.ankitsuda.base.ui

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

private fun Color.toHexString(): String {
    val alphaString = (alpha * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }
    val redString = (red * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }
    val greenString = (green * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }
    val blueString = (blue * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }

    return "#$alphaString$redString$greenString$blueString"
}

object ColorSerializer : KSerializer<Color> {
    override val descriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Color {
        val decodedString = decoder.decodeString();
        val finalString = if (decodedString.startsWith("#")) {
            decodedString
        } else {
            "#$decodedString"
        }
        return Color(android.graphics.Color.parseColor(finalString));
    }

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeString(value.toHexString())
    }
}

@Parcelize
@Serializable
data class ThemeState(
    @SerialName("isLightTheme")
    var isLightTheme: Boolean = true,
    @SerialName("isDarkStatusBarIcons")
    var isDarkStatusBarIcons: Boolean = true,
    @SerialName("isDarkNavigationBarIcons")
    var isDarkNavigationBarIcons: Boolean = true,

    @SerialName("primaryColor")
    @Serializable(with = ColorSerializer::class)
    var primaryColor: @RawValue Color = Color(41, 121, 255),
    @SerialName("backgroundColor")
    @Serializable(with = ColorSerializer::class)
    var backgroundColor: @RawValue Color = Color.White,
    @SerialName("onPrimaryColor")
    @Serializable(with = ColorSerializer::class)
    var onPrimaryColor: @RawValue Color = Color.White,
    @SerialName("onBackgroundColor")
    @Serializable(with = ColorSerializer::class)
    var onBackgroundColor: @RawValue Color = Color.Black,
    @SerialName("cardBorderColor")
    @Serializable(with = ColorSerializer::class)
    var cardBorderColor: @RawValue Color = Color.Gray,
    @SerialName("topBarContentColor")
    @Serializable(with = ColorSerializer::class)
    var topBarContentColor: @RawValue Color = Color.Black,
    @SerialName("topBarBackgroundColor")
    @Serializable(with = ColorSerializer::class)
    var topBarBackgroundColor: @RawValue Color = Color.White,
    @SerialName("cardColor")
    @Serializable(with = ColorSerializer::class)
    var cardColor: @RawValue Color = Color(248, 248, 248),

    @SerialName("topBarTitleAlignment")
    var topBarTitleAlignment: String = "center",
    @SerialName("bottomBarLabelWeight")
    var bottomBarLabelWeight: String = "bold",
    @SerialName("bottomBarLabelVisible")
    var bottomBarLabelVisible: String = "always",

    @SerialName("cardElevation")
    var cardElevation: Int = 0,
    @SerialName("cardBorderWidth")
    var cardBorderWidth: Int = 0,
    @SerialName("topBarElevation")
    var topBarElevation: Int = 0,
    @SerialName("bottomBarIconSize")
    var bottomBarIconSize: Int = 24,

    /* Shapes */
    @SerialName("shapeSmallTopStartRadius")
    var shapeSmallTopStartRadius: Int = 8,
    @SerialName("shapeSmallTopEndRadius")
    var shapeSmallTopEndRadius: Int = 8,
    @SerialName("shapeSmallBottomStartRadius")
    var shapeSmallBottomStartRadius: Int = 8,
    @SerialName("shapeSmallBottomEndRadius")
    var shapeSmallBottomEndRadius: Int = 8,

    /* Charts */
    @SerialName("chartsShaderEnabled")
    var chartsShaderEnabled: Boolean = true,
    @SerialName("chartsLineThickness")
    var chartsLineThickness: Int = 0,
    @SerialName("chartsPointDiameter")
    var chartsPointDiameter: Int = 0,
    @SerialName("chartsPointLineThickness")
    var chartsPointLineThickness: Int = 0,
) : Parcelable