package com.ankitsuda.rebound.ui.screens.personalization.main_colors

import androidx.annotation.Px
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BubbleChart
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect.Companion.Zero
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ankitsuda.rebound.ui.Route
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.screens.more.SectionHeader
import timber.log.Timber
import android.graphics.Color as AndroidColor

@Composable
fun MainColorsPersonalizationScreen(navController: NavController) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    var colorPickedVisible by remember {
        mutableStateOf(false)
    }
    var newPrimaryColor by remember {
        mutableStateOf(Color.Blue)
    }

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Main Colors", leftIconBtn = {
                TopBarBackIconButton {
                    navController.popBackStack()
                }
            }, rightIconBtn = {
                TopBarIconButton(icon = Icons.Outlined.Restore, title = "Reset to defaults") {

                }
            })
        },
    ) {

        val itemModifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                if (colorPickedVisible) {

                    ColorPicker(colorSelected = {
                        newPrimaryColor = it
                        Timber.d("new $it now $newPrimaryColor")
                    })

                }
            }
            item {
                ColorPickerCardItem(
                    modifier = itemModifier,
                    text = "Primary Color",
                    selectedColor = newPrimaryColor,
                    onNewColorSelected = {

                    },
                    onClick = {
                        colorPickedVisible = !colorPickedVisible
                    })
            }
            item {
                ColorPickerCardItem(
                    modifier = itemModifier,
                    text = "Background Color",
                    selectedColor = MaterialTheme.colors.background,
                    onNewColorSelected = {

                    })
            }
            item {
                ColorPickerCardItem(
                    modifier = itemModifier,
                    text = "On Primary Color",
                    selectedColor = MaterialTheme.colors.onPrimary,
                    onNewColorSelected = {

                    })
            }
            item {
                ColorPickerCardItem(
                    modifier = itemModifier,
                    text = "On Background Color",
                    selectedColor = MaterialTheme.colors.onBackground,
                    onNewColorSelected = {

                    })
            }
        }
    }

}


/**
 * Gradient color picker
 *
 * @sample dev.sasikanth.colorpicker.ColorPickerPreview
 */
@Composable
fun ColorPicker(
    pickerHeight: Dp = 24.dp,
    colorSelected: (Color) -> Unit
) {
    val maxWidth = with(LocalDensity.current) {
        100.dp.toPx()
    }

    // Since we start from hue 0 setting the default selected color
    // to red, but need to find a better way of getting initial color.
    var selectedColor by remember {

        mutableStateOf(Color.Red)
    }
    var position by remember {
        mutableStateOf(0f)
    }

    val squareSizePx = with(LocalDensity.current) {
        pickerHeight.toPx()
    }
    val pickerWidthPx = with(LocalDensity.current) {
        100.dp.toPx()
    }

    val pickerMaxWidth = pickerWidthPx - squareSizePx

    val horizontalGradient = LinearGradientShader(
        colors = colors(),
        from = Offset.Zero,
        to = Offset(pickerWidthPx, 0f)
    )
    val roundedCornerShape = RoundedCornerShape(pickerHeight / 2)

    val dragState = rememberDraggableState(onDelta = { delta ->
        val old = position
        // Making sure the sum of delta and position is within the 0 and
        // max width of picker view
        position = (delta + position).coerceIn(0f, pickerMaxWidth)

        val hsvColor = getHsvColor(position, pickerMaxWidth)
        selectedColor = Color(hsvColor)
        colorSelected(selectedColor)

        position - old
    })

    val drag = Modifier.draggable(
        state = dragState,
        orientation = Orientation.Horizontal,
        onDragStarted = {
            position = it.x
        },
    )

    // Color Picker View
    Box(
        modifier = drag
            .pointerInput(Unit) {

                detectTapGestures {
                    position = it.x

                    val hsvColor = getHsvColor(position, pickerMaxWidth)
                    selectedColor = Color(hsvColor)
                    colorSelected(selectedColor)
                }
            }

            .height(pickerHeight)
            .width(100.dp)
            .clip(shape = roundedCornerShape)
            .background(
                brush = Brush.horizontalGradient(
                    colors = colors(),
                    startX = 0f,
                    endX = pickerWidthPx
                ), shape = roundedCornerShape
            )
    ) {

        val xOffset = with(LocalDensity.current) { position.toDp() }
        val squareSize = with(LocalDensity.current) { squareSizePx.toDp() }

        // Square box to show the preview of selected color
        Box(
            Modifier
                .offset(x = xOffset, y = 0.dp)
                .width(squareSize)
                .height(squareSize)
                .clip(roundedCornerShape)
                .background(selectedColor)
                .border(width = 2.dp, color = Color.White, shape = roundedCornerShape)
                .shadow(elevation = 2.dp, shape = roundedCornerShape),
        )


    }
}

private fun getHsvColor(position: Float, maxWidth: Float): Int {
    // dividing the position of drag by max width of the picker view
    // and then we multiply it by 359 which is the final HUE value
    val hue = (position / maxWidth) * 359f
    return AndroidColor.HSVToColor(floatArrayOf(hue, 1f, 1f))
}


fun colors(): List<Color> {
    val colorRange = 0..359
    return colorRange.map {
        val hsvColor = AndroidColor.HSVToColor(floatArrayOf(it.toFloat(), 1f, 1f))
        Color(hsvColor)
    }
}