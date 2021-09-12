package com.ankitsuda.rebound.ui.components.color_picker



import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
public fun ColorPicker(
    modifier: Modifier = Modifier,
    alpha: Float = 1F,
    brightness: Float = 1F,
    magnifier: ColorPicker.Magnifier = ColorPicker.Magnifier.Options(),
    resetSelectedPosition: Boolean = false,
    onColorSelected: (Color) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1F)
    ) {
        val diameter = constraints.maxWidth

        var selectedPosition by remember { mutableStateOf(Offset.Zero) }
        if(resetSelectedPosition) {
            selectedPosition = Offset.Zero
        }

        val colorWheel = remember(diameter, alpha, brightness) {
            ColorWheel(
                diameter = diameter,
                alpha = alpha,
                brightness = brightness
            ).apply {
                val currentColor = colorForPosition(selectedPosition)
                if(currentColor.isSpecified) {
                    onColorSelected(currentColor)
                }
            }
        }

        val inputModifier = Modifier
            .fillMaxSize()
            .pointerInput(alpha, brightness) {
                fun update(newPosition: Offset) {
                    val color = colorWheel.colorForPosition(newPosition)
                    onColorSelected(color)
                    selectedPosition = when {
                        color.isSpecified -> newPosition
                        else -> Offset.Zero
                    }
                }

                forEachGesture {
                    awaitPointerEventScope {
                        val down = awaitFirstDown()
                        update(down.position)
                        drag(down.id) { change ->
                            change.consumePositionChange()
                            update(change.position)
                        }
                    }
                }
            }

        Box(inputModifier) {
            Image(contentDescription = null, bitmap = colorWheel.image)
            val color = colorWheel.colorForPosition(selectedPosition)
            if(magnifier is ColorPicker.Magnifier.Options && color.isSpecified) {
                Magnifier(
                    options = magnifier,
                    visible = selectedPosition != Offset.Zero,
                    position = selectedPosition,
                    color = color
                )
            }
        }
    }
}

public object ColorPicker {
    @Immutable
    public sealed class Magnifier {
        @Immutable
        public object None : Magnifier()

        @Immutable
        public data class Options(
            val width: Dp = 150.dp,
            val height: Dp = 100.dp,
            val labelHeight: Dp = 50.dp,
            val selectionCircleDiameter: Dp = 15.dp,
            val showAlpha: Boolean = true,
            val colorWidthWeight: Float = .25F,
            val hexWidthWeight: Float = .75F,
            val popupShape: GenericShape = MagnifierPopupShape
        ) : Magnifier()
    }
}

@Composable
private fun Magnifier(
    options: ColorPicker.Magnifier.Options,
    visible: Boolean,
    position: Offset,
    color: Color
) {
    val offset = with(LocalDensity.current) {
        Modifier.offset(
            position.x.toDp() - options.width / 2,
            // Align with the center of the selection circle
            position.y.toDp() - (options.height - options.selectionCircleDiameter / 2)
        )
    }
    MagnifierTransition(
        visible,
        options.width,
        options.selectionCircleDiameter
    ) { labelWidth: Dp, selectionDiameter: Dp,
        alpha: Float ->
        Column(
            offset.size(width = options.width, height = options.height)
                .alpha(alpha)
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                MagnifierLabel(Modifier.size(labelWidth, options.labelHeight), options, color)
            }
            Spacer(Modifier.weight(1f))
            Box(
                Modifier.fillMaxWidth().height(options.selectionCircleDiameter),
                contentAlignment = Alignment.Center
            ) {
                MagnifierSelectionCircle(Modifier.size(selectionDiameter), color)
            }
        }
    }
}

/**
 * [Transition] that animates between [visible] states of the magnifier by animating the width of
 * the label, diameter of the selection circle, and alpha of the overall magnifier
 */
@Composable
private fun MagnifierTransition(
    visible: Boolean,
    maxWidth: Dp,
    maxDiameter: Dp,
    content: @Composable (labelWidth: Dp, selectionDiameter: Dp, alpha: Float) -> Unit
) {
    val transition = updateTransition(visible)
    val labelWidth by transition.animateDp(transitionSpec = { tween() }) {
        if(it) maxWidth else 0.dp
    }
    val magnifierDiameter by transition.animateDp(transitionSpec = { tween() }) {
        if(it) maxDiameter else 0.dp
    }
    val alpha by transition.animateFloat(
        transitionSpec = {
            if(true isTransitioningTo false) {
                tween(delayMillis = 100, durationMillis = 200)
            }
            else {
                tween()
            }
        }
    ) {
        if(it) 1f else 0f
    }
    content(labelWidth, magnifierDiameter, alpha)
}

/**
 * Label representing the currently selected [color], with [Text] representing the hex code and a
 * square at the start showing the [color].
 */
@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun MagnifierLabel(modifier: Modifier, options: ColorPicker.Magnifier.Options, color: Color) {
    Surface(shape = options.popupShape, elevation = 4.dp) {
        Row(modifier) {
            Box(Modifier.weight(options.colorWidthWeight).fillMaxHeight().background(color))
            val text = "#" + color.toHexString().uppercase().let { hex ->
                if(options.showAlpha) hex else hex.drop(2)
            }
            val textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            Text(
                text = text,
                modifier = Modifier.weight(options.hexWidthWeight).padding(top = 10.dp, bottom = 20.dp, end = 5.dp),
                style = textStyle,
                maxLines = 1
            )
        }
    }
}

private fun Color.toHexString(): String {
    val alphaString = (alpha * 255).toInt().toString(16).let { if(it.length == 1) "0$it" else it }
    val redString = (red * 255).toInt().toString(16).let { if(it.length == 1) "0$it" else it }
    val greenString = (green * 255).toInt().toString(16).let { if(it.length == 1) "0$it" else it }
    val blueString = (blue * 255).toInt().toString(16).let { if(it.length == 1) "0$it" else it }

    return "$alphaString$redString$greenString$blueString"
}

/**
 * Selection circle drawn over the currently selected pixel of the color wheel.
 */
@Composable
private fun MagnifierSelectionCircle(modifier: Modifier, color: Color) {
    Surface(
        modifier,
        shape = CircleShape,
        elevation = 4.dp,
        color = color,
        border = BorderStroke(2.dp, SolidColor(Color.Black.copy(alpha = 0.75f))),
        content = {}
    )
}

/**
 * A [GenericShape] that draws a box with a triangle at the bottom center to indicate a popup.
 */
private val MagnifierPopupShape = GenericShape { size, _ ->
    val width = size.width
    val height = size.height

    val arrowY = height * 0.8f
    val arrowXOffset = width * 0.4f

    addRoundRect(RoundRect(0f, 0f, width, arrowY, cornerRadius = CornerRadius(20f, 20f)))

    moveTo(arrowXOffset, arrowY)
    lineTo(width / 2f, height)
    lineTo(width - arrowXOffset, arrowY)
    close()
}

/**
 * A color wheel with an [ImageBitmap] that draws a circular color wheel of the specified diameter.
 */
private class ColorWheel(diameter: Int, alpha: Float, brightness: Float) {
    private val radius = diameter / 2f

    private fun Color.applyBrightnessAndAlpha(alpha: Float, brightness: Float) =
        copy(
            red = red * brightness,
            green = green * brightness,
            blue = blue * brightness,
            alpha = alpha
        )

    private val sweepGradient = SweepGradientShader(
        colors = listOf(
            Color.Red.applyBrightnessAndAlpha(alpha, brightness),
            Color.Magenta.applyBrightnessAndAlpha(alpha, brightness),
            Color.Blue.applyBrightnessAndAlpha(alpha, brightness),
            Color.Cyan.applyBrightnessAndAlpha(alpha, brightness),
            Color.Green.applyBrightnessAndAlpha(alpha, brightness),
            Color.Yellow.applyBrightnessAndAlpha(alpha, brightness),
            Color.Red.applyBrightnessAndAlpha(alpha, brightness)
        ),
        colorStops = null,
        center = Offset(radius, radius)
    )

    private val saturationGradient = RadialGradientShader(
        colors = listOf(
            Color.White.applyBrightnessAndAlpha(alpha, brightness),
            Color.Transparent
        ),
        center = Offset(radius, radius),
        radius = radius
    )

    val image = ImageBitmap(diameter, diameter).also { imageBitmap ->
        val canvas = Canvas(imageBitmap)
        val center = Offset(radius, radius)
        val sweepPaint = Paint().apply {
            shader = sweepGradient
        }

        val saturationPaint = Paint().apply {
            shader = saturationGradient
        }

        canvas.drawCircle(center, radius, sweepPaint)
        canvas.drawCircle(center, radius, saturationPaint)
    }
}

/**
 * @return the matching color for [position] inside [ColorWheel], or `null` if there is no color
 * or the color is partially transparent.
 */
private fun ColorWheel.colorForPosition(position: Offset): Color {
    val x = position.x.toInt()
    val y = position.y.toInt()
    with(image.toPixelMap()) {
        if(x !in 0 until width || y !in 0 until height) return Color.Unspecified
        return this[x, y].takeIf { it.alpha > 0F } ?: Color.Unspecified
    }
}