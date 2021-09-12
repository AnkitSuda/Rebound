package com.ankitsuda.rebound.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.ankitsuda.rebound.data.datastore.PrefStorage
import javax.inject.Inject

private val DarkColorPalette = darkColors(
    primary = DefaultAccentColor,
    primaryVariant = Purple700,
    secondary = DefaultAccentColor
)

private val LightColorPalette = lightColors(
    primary = DefaultAccentColor,
    primaryVariant = Purple700,
    secondary = DefaultAccentColor

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@SuppressLint("ConflictingOnColor")
@Composable
fun ReboundTheme(
    darkTheme: Boolean = false/*isSystemInDarkTheme()*/,
    prefStorage: PrefStorage,
    content: @Composable() () -> Unit
) {
    val primaryColor by prefStorage.primaryColor.collectAsState(initial = DefaultAccentColor)
    val backgroundColor by prefStorage.backgroundColor.collectAsState(initial = Color.White)
    val onPrimaryColor by prefStorage.onPrimaryColor.collectAsState(initial = Color.White)
    val onBackgroundColor by prefStorage.onBackgroundColor.collectAsState(initial = Color.Black)

    val colors = lightColors(
        primary = primaryColor,
        primaryVariant = Purple700,
        secondary = primaryColor,
        onPrimary = onPrimaryColor,
        onBackground = onBackgroundColor,
        background = backgroundColor,
    )


    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}