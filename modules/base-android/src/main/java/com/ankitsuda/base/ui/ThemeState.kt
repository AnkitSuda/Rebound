package com.ankitsuda.base.ui

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import kotlinx.parcelize.Parcelize
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
    var isLightTheme: Boolean = false,
    @SerialName("isDarkStatusBarIcons")
    var isDarkStatusBarIcons: Boolean = true,
    @SerialName("isDarkNavigationBarIcons")
    var isDarkNavigationBarIcons: Boolean = true,

    @SerialName("primaryColor")
    @Serializable(with = ColorSerializer::class)
    var primaryColor: Color = Color(41, 121, 255),
    @SerialName("backgroundColor")
    @Serializable(with = ColorSerializer::class)
    var backgroundColor: Color = Color.White,
    @SerialName("onPrimaryColor")
    @Serializable(with = ColorSerializer::class)
    var onPrimaryColor: Color = Color.White,
    @SerialName("onBackgroundColor")
    @Serializable(with = ColorSerializer::class)
    var onBackgroundColor: Color = Color.Black,
    @SerialName("card")
    @Serializable(with = ColorSerializer::class)
    var card: Color = Color(248, 248, 248),
    @SerialName("cardBorderColor")
    @Serializable(with = ColorSerializer::class)
    var cardBorderColor: Color = Color.Gray,
    @SerialName("topBarContentColor")
    @Serializable(with = ColorSerializer::class)
    var topBarContentColor: Color = Color.Black,
    @SerialName("topBarBackgroundColor")
    @Serializable(with = ColorSerializer::class)
    var topBarBackgroundColor: Color = Color.White,

    @SerialName("topBarTitleAlignment")
    var topBarTitleAlignment: String = "center",

    var cardElevation: Int = 0,
    var cardBorderWidth: Int = 0,
    var topBarElevation: Int = 0,

    var shapeSmallTopLeftRadius: Int = 8,
    var shapeSmallTopRightRadius: Int = 8,
    var shapeSmallBottomLeftRadius: Int = 8,
    var shapeSmallBottomRightRadius: Int = 8,
) : Parcelable