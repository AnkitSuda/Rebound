package com.ankitsuda.rebound.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import android.annotation.SuppressLint
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import com.ankitsuda.rebound.ui.components.AppTextField
import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog


/**
 * Currently experimenting with solutions so the code is not clean
 */
@SuppressLint("NewApi")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorPickerAltDialog(
    defaultColor: Color = Color.Red,
    colorSelected: (Color) -> Unit
) {

    // Since we start from hue 0 setting the default selected color
    // to red, but need to find a better way of getting initial color.
    var selectedColor by remember {

        mutableStateOf(defaultColor)
    }
    var colorHexInfoHeight by remember {
        mutableStateOf(30.dp)
    }


    // Color Picker View
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            SatValPicker(selectedColor, onColorSelected = {
                selectedColor = it
            })

            Spacer(Modifier.height(16.dp))

            HueSlider(defaultColor = defaultColor, onHueSelected = { newColor ->
                selectedColor = newColor
            })

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .height(colorHexInfoHeight)
                        .width(colorHexInfoHeight)
                        .background(selectedColor)
                )
                Spacer(modifier = Modifier.width(16.dp))
                ColorHexInfo(
                    modifier = with(LocalDensity.current) {
                        Modifier.onGloballyPositioned {
                            colorHexInfoHeight = it.size.height.toDp()
                        }
                    },
                    selectedColor,
                    onHexEdited = {
                        selectedColor = it
                    })
            }

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Presets")
                }
                TextButton(onClick = with(LocalDialog.current) {
                    {
                        hideDialog()
                        colorSelected(selectedColor)
                    }
                }) {
                    Text(text = "Select")
                }
            }
        }
    }
}

@Composable
private fun ColorHexInfo(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    onHexEdited: (Color) -> Unit
) {
    var mSelectedColor by remember {
        mutableStateOf(selectedColor)
    }
    var text by remember {
        mutableStateOf("#" + mSelectedColor.toHexString().uppercase().drop(2))
    }
    if (selectedColor != mSelectedColor) {
        mSelectedColor = selectedColor
        text = "#" + mSelectedColor.toHexString().uppercase().drop(2)
    }
    AppTextField(modifier = modifier, value = text, placeholderValue = "Hex", onValueChange = {
        text = it
        try {
            onHexEdited(Color(AndroidColor.parseColor(it)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    })
}

private fun Color.toHexString(): String {
    val alphaString = (alpha * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }
    val redString = (red * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }
    val greenString = (green * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }
    val blueString = (blue * 255).toInt().toString(16).let { if (it.length == 1) "0$it" else it }

    return "$alphaString$redString$greenString$blueString"
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SatValPicker(color: Color, onColorSelected: (Color) -> Unit) {
    val hsvArray = FloatArray(3)
    AndroidColor.colorToHSV(color.toArgb(), hsvArray)
    val hue = hsvArray[0]
    var size by remember { mutableStateOf(Size(100f, 100f)) }
    var selectedColor by remember {
        mutableStateOf(color)
    }

    var mVal by remember {
        mutableStateOf(hsvArray[2])
    }
    var mSat by remember {
        mutableStateOf(hsvArray[1])
    }

    var position by remember {
        mutableStateOf(Offset(mVal, mSat))
    }

    position = Offset(mSat * size.width, size.height - (mVal * size.height));


    val drag = Modifier
        .pointerInteropFilter {
            position = Offset(it.x.coerceIn(0f, size.width), it.y.coerceIn(0f, size.height))
            Timber.d(it.toString())
            mSat = (it.x) / (size.width)
            mVal = ((size.width - it.y)) / (size.width)

            selectedColor = Color(
                AndroidColor.HSVToColor(
                    floatArrayOf(
                        hue,
                        mSat,
                        mVal
                    )
                )
            )

            onColorSelected(selectedColor)

            true
        }


    // Hsv picker
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .onGloballyPositioned {
                size = it.size.toSize()
            }
    ) {
        with(
            LocalDensity.current
        ) {
            Image(
                modifier = drag
                    .fillMaxSize()
                    .align(Alignment.Center),
                bitmap =
                getSatValBitmap(
                    hue = hue,
                    width = size.width.toInt(),
                    height = size.height.toInt(),
                    skipCount = 10,
                )!!.asImageBitmap(),
                contentDescription = ""
            )

            val xOffset = with(LocalDensity.current) { position.x.toDp() }
            val yOffset = with(LocalDensity.current) { position.y.toDp() }
            val squareSize = 16.dp

            Box(
                Modifier
                    .offset(x = xOffset, y = yOffset)
                    .width(squareSize)
                    .height(squareSize)
                    .clip(CircleShape)
                    .background(color = selectedColor, shape = CircleShape)
                    .border(width = 2.dp, color = Color.White, shape = CircleShape)
                    .shadow(elevation = 2.dp),
            )


        }
    }
}


@Composable
fun HueSlider(defaultColor: Color, onHueSelected: (color: Color) -> Unit) {

    val pickerHeight: Dp = 24.dp

    var selectedColor by remember {
        mutableStateOf(Color(AndroidColor.HSVToColor(floatArrayOf(defaultColor.hue(), 1f, 1f))))
    }

    val squareSizePx =
        with(LocalDensity.current) {
            pickerHeight.toPx()

        }

    var pickerWidthPx by with(LocalDensity.current) {
        remember {
            mutableStateOf(300.dp.toPx())
        }
    }


    val pickerMaxWidth = pickerWidthPx - squareSizePx

    var position by remember {
        mutableStateOf(pickerMaxWidth - defaultColor.hue())
    }

    val dragState = rememberDraggableState(onDelta = { delta ->
        val old = position
        // Making sure the sum of delta and position is within the 0 and
        // max width of picker view
        position = (delta + position).coerceIn(0f, pickerMaxWidth)

        val hsvColor = getHsvColor(position, pickerMaxWidth)
        val newSelectedColor = Color(hsvColor)

        onHueSelected(newSelectedColor/*, hsvArray[0]*/)
        selectedColor = newSelectedColor
        position - old
    })

    val drag = Modifier
        .draggable(
            state = dragState,
            orientation = Orientation.Horizontal,
            onDragStarted = {
                position = it.x
            },
        )
        .pointerInput(Unit) {

            detectTapGestures {
                position = it.x

                val hsvColor = getHsvColor(position, pickerMaxWidth)
                val newSelectedColor = Color(hsvColor)

                onHueSelected(newSelectedColor/*, hsvArray[0]*/)
                selectedColor = newSelectedColor

            }
        }

    // Hue picker
    Box(
        modifier = drag
            .height(pickerHeight)
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = hueColors(),
                    startX = 0f,
                    endX = pickerWidthPx
                ),
            )
            .onGloballyPositioned {
                pickerWidthPx = it.size.width.toFloat()
            }
    ) {

        val xOffset = with(LocalDensity.current) { position.toDp() }
        val squareSize = with(LocalDensity.current) { squareSizePx.toDp() }

        // Square box to show the preview of selected color
        Box(
            Modifier
                .offset(x = xOffset, y = 0.dp)
                .width(squareSize)
                .height(squareSize)
                .background(selectedColor)
                .border(width = 2.dp, color = Color.White)
                .shadow(elevation = 2.dp),
        )


    }

}

fun Color.hue(): Float {
    val hsvArray = FloatArray(3)
    AndroidColor.colorToHSV(this.toArgb(), hsvArray)
    return hsvArray[0]
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
private fun getSatValBitmap(hue: Float, width: Int, height: Int, skipCount: Int): Bitmap? {
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