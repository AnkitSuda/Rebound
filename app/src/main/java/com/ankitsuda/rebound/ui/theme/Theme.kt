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
import com.ankitsuda.rebound.data.datastore.PrefStorage

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
    prefStorage: PrefStorage,
    content: @Composable () -> Unit
) {
    // Colors
    val primaryColor by prefStorage.primaryColor.collectAsState(initial = DefaultAccentColor)
    val backgroundColor by prefStorage.backgroundColor.collectAsState(initial = Color.White)
    val onPrimaryColor by prefStorage.onPrimaryColor.collectAsState(initial = Color.White)
    val onBackgroundColor by prefStorage.onBackgroundColor.collectAsState(initial = Color.Black)
    val card by prefStorage.cardColor.collectAsState(initial = Color(248, 248, 248))
    val cardBorderColor by prefStorage.cardBorderColor.collectAsState(initial = Color.Gray)

    // Dimens
    val cardElevation by prefStorage.cardElevation.collectAsState(initial = 0)
    val cardBorderWidth by prefStorage.cardBorderWidth.collectAsState(initial = 0)

    // Shape values
    val shapeSmallTopLeftRadius by prefStorage.shapeSmallTopStartRadius.collectAsState(initial = 0)
    val shapeSmallTopRightRadius by prefStorage.shapeSmallTopEndRadius.collectAsState(initial = 0)
    val shapeSmallBottomLeftRadius by prefStorage.shapeSmallBottomStartRadius.collectAsState(initial = 0)
    val shapeSmallBottomRightRadius by prefStorage.shapeSmallBottomEndRadius.collectAsState(
        initial = 0
    )


    // Animated colors
    val animatedBackgroundColor by animateColorAsState(targetValue = backgroundColor)

    val colors = lightReboundColors(
        primary = primaryColor,
        onPrimary = onPrimaryColor,
        onBackground = onBackgroundColor,
        background = animatedBackgroundColor,
        card = card,
        cardBorder = cardBorderColor,
    )

    val dimens = defaultDimens(
        cardElevation = cardElevation.dp,
        cardBorderWidth = cardBorderWidth.dp,
    )

    val shapes = Shapes(
        small = RoundedCornerShape(
            topStart = shapeSmallTopLeftRadius.dp,
            topEnd = shapeSmallTopRightRadius.dp,
            bottomStart = shapeSmallBottomLeftRadius.dp,
            bottomEnd = shapeSmallBottomRightRadius.dp
        ),
        medium = RoundedCornerShape(
            topStart = shapeSmallTopLeftRadius.dp,
            topEnd = shapeSmallTopRightRadius.dp,
            bottomStart = shapeSmallBottomLeftRadius.dp,
            bottomEnd = shapeSmallBottomRightRadius.dp
        ),
        large = RoundedCornerShape(
            topStart = shapeSmallTopLeftRadius.dp,
            topEnd = shapeSmallTopRightRadius.dp,
            bottomStart = shapeSmallBottomLeftRadius.dp,
            bottomEnd = shapeSmallBottomRightRadius.dp
        ),
    )

    ReboundTheme(
        colors = colors,
        dimens = dimens,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

@Composable
fun ReboundTheme(
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
    isLight: Boolean
) {
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
    var isLight by mutableStateOf(isLight, structuralEqualityPolicy())
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
        isLight: Boolean = this.isLight
    ): ReboundColors = ReboundColors(
        primary,
        background,
        error,
        onPrimary,
        onBackground,
        card,
        cardMainContent,
        cardSecondaryContent,
        cardBorder,
        topBar,
        topBarTitle,
        topBarSubtitle,
        topBarIcons,
        onError,
        isLight
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

internal val LocalReboundColors = staticCompositionLocalOf { lightReboundColors() }
internal val LocalReboundDimens = staticCompositionLocalOf { defaultDimens() }
internal val LocalReboundTypography = staticCompositionLocalOf { Typography() }

fun defaultDimens(cardElevation: Dp = 0.dp, cardBorderWidth: Dp = 0.dp): ReboundDimens =
    ReboundDimens(
        cardElevation = cardElevation,
        cardBorderWidth = cardBorderWidth
    )

fun lightReboundColors(
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
    isLight = true,
    card = card,
    cardBorder = cardBorder,
    cardMainContent = cardMainContent,
    cardSecondaryContent = cardSecondaryContent,
    topBar = topBar,
    topBarTitle = topBarTitle,
    topBarSubtitle = topBarSubtitle,
    topBarIcons = topBarIcons,
)

fun darkReboundColors(
    primary: Color = Color(0xFFBB86FC),
    secondary: Color = Color(0xFF03DAC6),
    background: Color = Color(0xFF121212),
    error: Color = Color(0xFFCF6679),
    onPrimary: Color = Color.Black,
    onBackground: Color = Color.White,
    onError: Color = Color.Black,
    // Not actual dark colors
    card: Color = Color.White,
    cardMainContent: Color = Color.Black,
    cardSecondaryContent: Color = Color.Black,
    cardBorder: Color = Color.Black,
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
    isLight = true,
    card = card,
    cardMainContent = cardMainContent,
    cardSecondaryContent = cardSecondaryContent,
    cardBorder = cardBorder,
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
    card = other.card
    cardMainContent = other.cardMainContent
    cardSecondaryContent = other.cardSecondaryContent
    cardBorder = other.cardBorder
    topBar = other.topBar
    topBarTitle = other.topBarTitle
    topBarSubtitle = other.topBarSubtitle
    topBarIcons = other.topBarIcons
}