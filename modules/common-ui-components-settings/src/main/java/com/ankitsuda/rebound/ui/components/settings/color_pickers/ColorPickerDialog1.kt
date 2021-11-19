package com.ankitsuda.rebound.ui.components.settings.color_pickers

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.ankitsuda.common.compose.LocalDialog
import com.ankitsuda.rebound.ui.components.color_picker.ColorPicker1
import com.ankitsuda.rebound.ui.components.color_picker.ColorPickerPresets


@Composable
fun ColorPickerDialog1(
    defaultColor: Color = Color.Red,
    colorSelected: (Color) -> Unit
) {

    var isPresetLayout by remember {
        mutableStateOf(true)
    }

    var selectedColor by remember {
        mutableStateOf(defaultColor)
    }

    val screenHeight = (LocalConfiguration.current.screenHeightDp / 1.0).dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight)
    ) {
        if (isPresetLayout) {
            ColorPickerPresets(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                onColorPicked = {
                    selectedColor = it
                },
            )

        } else {
            ColorPicker1(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp),
                colorSelected = {
                    selectedColor = it
                },
                defaultColor = defaultColor
            )

        }


        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = {
                isPresetLayout = !isPresetLayout
            }) {
                Text(
                    text = if (isPresetLayout) "Custom" else "Presets"
                )
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