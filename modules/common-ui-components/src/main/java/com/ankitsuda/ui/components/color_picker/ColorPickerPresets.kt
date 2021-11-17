package com.ankitsuda.ui.components.color_picker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog
import com.ankitsuda.rebound.utils.lighterOrDarkerColor
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorPickerPresets(
    modifier: Modifier = Modifier,
    shadesEnabled: Boolean = true,
    onColorPicked: (Color) -> Unit
) {

    val colors by remember {
        mutableStateOf(presetColors())
    }

    Box(modifier = modifier) {
        val colorItemSize = 32.dp

        Column(Modifier.fillMaxWidth()) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                cells = GridCells.Adaptive(minSize = colorItemSize),
            ) {
                items(colors.size) {
                    val color = colors[it]
                    ColorPickerPresetColorItem(
                        size = colorItemSize,
                        color = color,
                        onSelect = {
                            onColorPicked(color)
                        }
                    )
                }
            }


        }

    }
}

@Composable
fun ColorPickerPresetColorItem(size: Dp, color: Color, onSelect: () -> Unit) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(shape = CircleShape, color = color)
            .border(width = 1.dp, shape = CircleShape, color = color.lighterOrDarkerColor(0.4f))
            .clickable(onClick = onSelect)
    )
}

private fun presetColors(): ArrayList<Color> {
    val colors = arrayListOf<Color>()
    repeat(20) {
        colors.add(Color(255, Random.nextInt(256), Random.nextInt(256)))
    }
    return colors
}