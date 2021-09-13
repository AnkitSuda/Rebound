package com.ankitsuda.rebound.ui.components.color_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import timber.log.Timber
import android.graphics.Color as AndroidColor
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInteropFilter

/**
 * Currently experimenting with solutions so the code is not clean
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorPickerAlt(
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
    var selectedColorHue by remember {

        mutableStateOf(0f)
    }
    var position by remember {
        mutableStateOf(0f)
    }

    val squareSizePx = with(LocalDensity.current) {
        pickerHeight.toPx()
    }
    val pickerWidthPx = with(LocalDensity.current) {
        300.dp.toPx()
    }

    val pickerMaxWidth = pickerWidthPx - squareSizePx

    val horizontalGradient = LinearGradientShader(
        colors = hueColors(),
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

        val hsvArray = FloatArray(3)
        AndroidColor.colorToHSV(selectedColor.toArgb(), hsvArray)
        selectedColorHue = hsvArray[0]
        Timber.d(hsvArray.toString())

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
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Hsv picker
            Box(modifier = Modifier.fillMaxWidth()) {
                with(
                    LocalDensity.current
                ) {
                    Image(
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp)
                            .align(Alignment.Center)
                            .pointerInteropFilter {
                                Timber.d(it.toString())
                                val mSat = (it.x) / (300.dp.toPx())
                                val mVal = ((300.dp.toPx() - it.y)) / (300.dp.toPx())

                                selectedColor =
                                    Color(
                                        AndroidColor.HSVToColor(
                                            floatArrayOf(
                                                selectedColorHue,
                                                mSat,
                                                mVal
                                            )
                                        )
                                    )

                                colorSelected(selectedColor)
                                true
                            },
                        bitmap =
                        getSatValBitmap(
                            hue = selectedColorHue,
                            width = 300.dp.toPx().toInt(),
                            height = 300.dp.toPx().toInt(),
                            skipCount = 10,
                        )!!.asImageBitmap(),
                        contentDescription = ""
                    )
                }
            }

            // Hue picker
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
                    .width(300.dp)
                    .clip(shape = roundedCornerShape)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = hueColors(),
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
    }
}


private fun getHsvColor(position: Float, maxWidth: Float): Int {
    // dividing the position of drag by max width of the picker view
    // and then we multiply it by 359 which is the final HUE value
    val hue = (position / maxWidth) * 359f
    return AndroidColor.HSVToColor(floatArrayOf(hue, 1f, 1f))
}


fun hueColors(): List<Color> {
    val colorRange = 0..359
    return colorRange.map {
        val hsvColor = AndroidColor.HSVToColor(floatArrayOf(it.toFloat(), 1f, 1f))
        Color(hsvColor)
    }
}


/**
 * Gets a Rectangular Bitmap representing the Gradient of Sat and Val for the given Hue
 * @param hue Value of Hue to use for the bitmap generation of SatVal Gradient bitmap
 * @param width Width of the SatValPicker
 * @param height Height of the SatValPicker
 * @param skipCount Number of pixels to skip when generating Bitmap (increasing this results in faster bitmap generation but reduces bitmap quality)
 * @return A Rectangular Bitmap representing the Gradient of Sat and Val for the given Hue
 */
fun getSatValBitmap(hue: Float, width: Int, height: Int, skipCount: Int): Bitmap? {
//        //System.out.println("Width2: " + width);
//        //System.out.println("Height2: " + height);
    Timber.d("hue $hue width $width height $height")
    val hueBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    val colors = IntArray(width * height)
    var pix = 0
    var y = 0
    while (y < height) {
        var x = 0
        while (x < width) {
            if (pix >= width * height) break
            val sat = x / width.toFloat()
            val `val` = (height - y) / height.toFloat()
            val hsv = floatArrayOf(hue, sat, `val`)
            val color: Int = AndroidColor.HSVToColor(hsv)
            for (m in 0 until skipCount) {
                if (pix >= width * height) break

//                    //System.out.println("Filling...");
                if (x + m < width) { /*
                        //System.out.println(x+n);
                        //System.out.println(width);*/
                    colors[pix] = color
                    pix++
                }
            }
            x += skipCount
        }
        for (n in 0 until skipCount) {
            if (pix >= width * height) break

//                    //System.out.println("Filling...");
            for (x in 0 until width) {
                colors[pix] = colors[pix - width]
                pix++
            }
        }
        y += skipCount
    }
    hueBitmap.setPixels(colors, 0, width, 0, 0, width, height)
    return hueBitmap
}