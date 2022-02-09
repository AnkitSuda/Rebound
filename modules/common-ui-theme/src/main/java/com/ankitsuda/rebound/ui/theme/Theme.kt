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

package com.ankitsuda.rebound.ui.theme

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.ui.ThemeState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = DefaultAccentColor,
    primaryVariant = Purple700,
    secondary = DefaultAccentColor
)

private val LightColorPalette = lightColors(
    primary = DefaultAccentColor,
    primaryVariant = Purple700,
    secondary = DefaultAccentColor
)


@SuppressLint("ConflictingOnColor")
@Composable
fun ReboundThemeWrapper(
    themeState: ThemeState,
    content: @Composable () -> Unit
) {
    // Animated colors
    val animatedBackgroundColor by animateColorAsState(targetValue = themeState.backgroundColor)

    val colors = lightReboundColors(
        isLight = themeState.isLightTheme,
        isDarkStatusBarIcons = themeState.isDarkStatusBarIcons,
        isDarkNavigationBarIcons = themeState.isDarkNavigationBarIcons,
        primary = themeState.primaryColor,
        onPrimary = themeState.onPrimaryColor,
        onBackground = themeState.onBackgroundColor,
        background = animatedBackgroundColor,
        card = themeState.cardColor,
        cardBorder = themeState.cardBorderColor,
        topBar = themeState.topBarBackgroundColor,
        topBarTitle = themeState.topBarContentColor
    )

    val dimens = defaultDimens(
        cardElevation = themeState.cardElevation.dp,
        cardBorderWidth = themeState.cardBorderWidth.dp,
    )

    val shapes = Shapes(
        small = RoundedCornerShape(
            topStart = themeState.shapeSmallTopStartRadius.dp,
            topEnd = themeState.shapeSmallTopEndRadius.dp,
            bottomStart = themeState.shapeSmallBottomStartRadius.dp,
            bottomEnd = themeState.shapeSmallBottomEndRadius.dp
        ),
        medium = RoundedCornerShape(
            topStart = themeState.shapeSmallTopStartRadius.dp,
            topEnd = themeState.shapeSmallTopEndRadius.dp,
            bottomStart = themeState.shapeSmallBottomStartRadius.dp,
            bottomEnd = themeState.shapeSmallBottomEndRadius.dp
        ),
        large = RoundedCornerShape(
            topStart = themeState.shapeSmallTopStartRadius.dp,
            topEnd = themeState.shapeSmallTopEndRadius.dp,
            bottomStart = themeState.shapeSmallBottomStartRadius.dp,
            bottomEnd = themeState.shapeSmallBottomEndRadius.dp
        ),
    )

//    val typography = createTypographyRubik(
//        colorH1 = onBackgroundColor,
//        colorH2 = onBackgroundColor,
//        colorH3 = onBackgroundColor,
//        colorH4 = onBackgroundColor,
//        colorH5 = onBackgroundColor,
//        colorH6 = onBackgroundColor,
//        colorSubtitle1 = onBackgroundColor.copy(alpha = 0.7f),
//        colorSubtitle2 = onBackgroundColor.copy(alpha = 0.7f),
//        colorBody1 = onBackgroundColor,
//        colorBody2 = onBackgroundColor,
//        colorButton = onPrimaryColor,
//        colorCaption = onBackgroundColor.copy(alpha = 0.5f),
//        colorOverline = onBackgroundColor.copy(alpha = 0.5f),
//    )

    // Getting instance of systemUiController
    val systemUiController = rememberSystemUiController()

    SideEffect {
        // Changing status bar icons as user pref
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = themeState.isDarkStatusBarIcons
        )

        // Changing navigation bar icons as user pref
        systemUiController.setNavigationBarColor(
            color = Color.Transparent,
            darkIcons = themeState.isDarkNavigationBarIcons
        )
    }

    ReboundTheme(
        themeState = themeState,
        colors = colors,
        dimens = dimens,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

@Composable
fun ReboundTheme(
    themeState: ThemeState,
    colors: ReboundColors,
    dimens: ReboundDimens,
    typography: Typography,
    shapes: Shapes,
    content: @Composable () -> Unit
) {
    val rememberedColors = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }.apply { updateColorsFrom(colors) }

    CompositionLocalProvider(
        LocalThemeState provides themeState,
        LocalReboundColors provides rememberedColors,
        LocalReboundDimens provides dimens,
        LocalReboundShapes provides shapes,
        LocalReboundTypography provides typography
    ) {
        MaterialTheme(
            colors = with(colors) {
                Colors(
                    primary = primary,
                    primaryVariant = primary,
                    secondary = primary,
                    secondaryVariant = primary,
                    background = background,
                    surface = background,
                    error = error,
                    onPrimary = onPrimary,
                    onSecondary = onPrimary,
                    onBackground = onBackground,
                    onSurface = onBackground,
                    onError = onError,
                    isLight = isLight
                )
            },
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

@Stable
class ReboundColors(
    isLight: Boolean,
    isDarkNavigationBarIcons: Boolean,
    isDarkStatusBarIcons: Boolean,
    primary: Color,
    background: Color,
    error: Color,
    onPrimary: Color,
    onBackground: Color,
    onError: Color,
    card: Color,
    cardMainContent: Color,
    cardSecondaryContent: Color,
    cardBorder: Color,
    topBar: Color,
    topBarTitle: Color,
    topBarSubtitle: Color,
    topBarIcons: Color,
) {
    var isLight by mutableStateOf(isLight, structuralEqualityPolicy())
        internal set
    var isDarkNavigationBarIcons by mutableStateOf(
        isDarkNavigationBarIcons,
        structuralEqualityPolicy()
    )
        internal set
    var isDarkStatusBarIcons by mutableStateOf(isDarkStatusBarIcons, structuralEqualityPolicy())
        internal set
    var primary by mutableStateOf(primary, structuralEqualityPolicy())
        internal set
    var background by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    var error by mutableStateOf(error, structuralEqualityPolicy())
        internal set
    var onPrimary by mutableStateOf(onPrimary, structuralEqualityPolicy())
        internal set
    var onBackground by mutableStateOf(onBackground, structuralEqualityPolicy())
        internal set
    var onError by mutableStateOf(onError, structuralEqualityPolicy())
        internal set
    var card by mutableStateOf(card, structuralEqualityPolicy())
        internal set
    var cardMainContent by mutableStateOf(cardMainContent, structuralEqualityPolicy())
        internal set
    var cardSecondaryContent by mutableStateOf(cardSecondaryContent, structuralEqualityPolicy())
        internal set
    var cardBorder by mutableStateOf(cardBorder, structuralEqualityPolicy())
        internal set
    var topBar by mutableStateOf(topBar, structuralEqualityPolicy())
        internal set
    var topBarTitle by mutableStateOf(topBarTitle, structuralEqualityPolicy())
        internal set
    var topBarSubtitle by mutableStateOf(topBarSubtitle, structuralEqualityPolicy())
        internal set
    var topBarIcons by mutableStateOf(topBarIcons, structuralEqualityPolicy())
        internal set

    /**
     * Returns a copy of this Colors, optionally overriding some of the values.
     */
    fun copy(
        primary: Color = this.primary,
        background: Color = this.background,
        error: Color = this.error,
        onPrimary: Color = this.onPrimary,
        onBackground: Color = this.onBackground,
        onError: Color = this.onError,
        card: Color = this.card,
        cardMainContent: Color = this.cardMainContent,
        cardSecondaryContent: Color = this.cardSecondaryContent,
        cardBorder: Color = this.cardBorder,
        topBar: Color = this.topBar,
        topBarTitle: Color = this.topBarTitle,
        topBarSubtitle: Color = this.topBarSubtitle,
        topBarIcons: Color = this.topBarIcons,
        isLight: Boolean = this.isLight,
        isDarkStatusBarIcons: Boolean = this.isDarkStatusBarIcons,
        isDarkNavigationBarIcons: Boolean = this.isDarkNavigationBarIcons
    ): ReboundColors = ReboundColors(
        isLight = isLight,
        isDarkStatusBarIcons = isDarkStatusBarIcons,
        isDarkNavigationBarIcons = isDarkNavigationBarIcons,
        primary = primary,
        background = background,
        error = error,
        onPrimary = onPrimary,
        onBackground = onBackground,
        card = card,
        cardMainContent = cardMainContent,
        cardSecondaryContent = cardSecondaryContent,
        cardBorder = cardBorder,
        topBar = topBar,
        topBarTitle = topBarTitle,
        topBarSubtitle = topBarSubtitle,
        topBarIcons = topBarIcons,
        onError = onError,
    )
}

@Stable
class ReboundDimens(
    cardElevation: Dp,
    cardBorderWidth: Dp,
) {
    var cardElevation by mutableStateOf(cardElevation, structuralEqualityPolicy())
        internal set
    var cardBorderWidth by mutableStateOf(cardBorderWidth, structuralEqualityPolicy())
        internal set
}


object ReboundTheme {
    val state: ThemeState
        @Composable
        get() = LocalThemeState.current

    val colors: ReboundColors
        @Composable
        @ReadOnlyComposable
        get() = LocalReboundColors.current

    val dimens: ReboundDimens
        @Composable
        @ReadOnlyComposable
        get() = LocalReboundDimens.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalReboundTypography.current

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = LocalReboundShapes.current
}

val LocalThemeState = staticCompositionLocalOf<ThemeState> {
    error("No LocalThemeState provided")
}
internal val LocalReboundColors = staticCompositionLocalOf { lightReboundColors() }
internal val LocalReboundDimens = staticCompositionLocalOf { defaultDimens() }
internal val LocalReboundTypography = staticCompositionLocalOf { Typography() }

fun defaultDimens(cardElevation: Dp = 0.dp, cardBorderWidth: Dp = 0.dp): ReboundDimens =
    ReboundDimens(
        cardElevation = cardElevation,
        cardBorderWidth = cardBorderWidth
    )

fun lightReboundColors(
    isLight: Boolean = false,
    isDarkStatusBarIcons: Boolean = true,
    isDarkNavigationBarIcons: Boolean = true,
    primary: Color = Color(0xFF6200EE),
    background: Color = Color.White,
    error: Color = Color(0xFFB00020),
    onPrimary: Color = Color.White,
    onBackground: Color = Color.Black,
    onError: Color = Color.White,
    card: Color = Color.White,
    cardBorder: Color = Color.Gray,
    cardMainContent: Color = Color.Black,
    cardSecondaryContent: Color = Color.Black,
    topBar: Color = Color.White,
    topBarTitle: Color = Color.Black,
    topBarSubtitle: Color = Color.Black,
    topBarIcons: Color = Color.Black,
): ReboundColors = ReboundColors(
    primary = primary,
    background = background,
    error = error,
    onPrimary = onPrimary,
    onBackground = onBackground,
    onError = onError,
    isLight = isLight,
    isDarkStatusBarIcons = isDarkStatusBarIcons,
    isDarkNavigationBarIcons = isDarkNavigationBarIcons,
    card = card,
    cardBorder = cardBorder,
    cardMainContent = cardMainContent,
    cardSecondaryContent = cardSecondaryContent,
    topBar = topBar,
    topBarTitle = topBarTitle,
    topBarSubtitle = topBarSubtitle,
    topBarIcons = topBarIcons,
)


fun ReboundColors.updateColorsFrom(other: ReboundColors) {
    primary = other.primary
    background = other.background
    error = other.error
    onPrimary = other.onPrimary
    onBackground = other.onBackground
    onError = other.onError
    isLight = other.isLight
    isDarkNavigationBarIcons = other.isDarkNavigationBarIcons
    isDarkStatusBarIcons = other.isDarkStatusBarIcons
    card = other.card
    cardMainContent = other.cardMainContent
    cardSecondaryContent = other.cardSecondaryContent
    cardBorder = other.cardBorder
    topBar = other.topBar
    topBarTitle = other.topBarTitle
    topBarSubtitle = other.topBarSubtitle
    topBarIcons = other.topBarIcons
}