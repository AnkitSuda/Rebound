package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.ankitsuda.rebound.ui.components.color_picker.ColorPicker
import com.ankitsuda.rebound.ui.dialogs.ColorPickerDialog
import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog

@Composable
fun ColorPickerCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    enableAutoColorPicker: Boolean = true,
    selectedColor: Color = Color.Black,
    onNewColorSelected: (Color) -> Unit = {},
    onClick: () -> Unit = {}
) {
    with(LocalDialog.current) {
        AppCard(modifier = modifier, onClick = {
            onClick()
            if (enableAutoColorPicker) {
                dialogContent = {
                    ColorPickerDialog(onColorSelected = onNewColorSelected)
                }
                showDialog()
            }
        }) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = text)
                    if (description.isNotEmpty()) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.caption,
                            color = Color(117, 117, 117)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                        .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                )
            }
        }
    }
}

@Composable
fun SwitchCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    checked: Boolean = false,
    onChange: (Boolean) -> Unit = {}
) {
    AppCard(modifier = modifier, onClick = {
        onChange(!checked)
    }) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = text)
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.caption,
                        color = Color(117, 117, 117)
                    )
                }
            }
            Switch(checked = checked, onCheckedChange = onChange)
        }
    }
}

@Composable
fun SliderCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    value: Float,
    steps: Int = 1,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onChange: (Float) -> Unit = {}
) {
    AppCard(modifier = modifier) {

        Row(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {

                Row {
                    Text(text = text, modifier = Modifier.weight(1f))
                    Text(text = value.toString())
                }
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.caption,
                        color = Color(117, 117, 117)
                    )
                }
                Slider(
                    value = value,
                    onValueChange = onChange,
                    valueRange = valueRange,
                    steps = steps,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}