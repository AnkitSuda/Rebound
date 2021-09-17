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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.dialogs.ColorPickerAltDialog
import com.ankitsuda.rebound.ui.dialogs.ColorPickerDialog
import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog
import com.ankitsuda.rebound.ui.theme.ShapeValues
import kotlin.math.roundToInt

@Composable
fun ColorPickerCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    enableAutoColorPicker: Boolean = true,
    useAltColorPicker: Boolean = true,
    selectedColor: Color = Color.Black,
    onNewColorSelected: (Color) -> Unit = {},
    onClick: () -> Unit = {}
) {
    with(LocalDialog.current) {
        AppCard(modifier = modifier, onClick = {
            onClick()
            if (enableAutoColorPicker) {
                dialogContent = {
                    if (useAltColorPicker) {
                        ColorPickerAltDialog(
                            defaultColor = selectedColor,
                            colorSelected = onNewColorSelected
                        )
                    } else {
                        ColorPickerDialog(onColorSelected = onNewColorSelected)
                    }
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
    roundValues: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onChange: (Float) -> Unit = {}
) {
    // Temporary fix for lag
    var mValue by remember {
        mutableStateOf(value)
    }

//    if (value != mValue) mValue = value

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
                    Text(text = mValue.toString())
                }
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.caption,
                        color = Color(117, 117, 117)
                    )
                }
                Slider(
                    value = mValue,
                    onValueChange = {
                        val newValue = if (roundValues) it.roundToInt().toFloat() else it
                        onChange(newValue)
                        mValue = newValue
                    },
                    valueRange = valueRange,
                    steps = steps,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun ShapesEditorCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    description: String = "",
    defaultValues: ShapeValues = ShapeValues(),
    onValueChange: (ShapeValues) -> Unit
) {
    var isTopLeftExpanded by remember {
        mutableStateOf(false)
    }
    var isTopRightExpanded by remember {
        mutableStateOf(false)
    }
    var isBottomLeftExpanded by remember {
        mutableStateOf(false)
    }
    var isBottomRightExpanded by remember {
        mutableStateOf(false)
    }

    var currentShapeValues by remember {
        mutableStateOf(defaultValues)
    }

    AppCard(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                Column(modifier = Modifier.fillMaxWidth()) {

                    Text(text = text)
                    if (description.isNotEmpty()) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.caption,
                            color = Color(117, 117, 117)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Box() {
                    Button(onClick = { isTopLeftExpanded = !isTopLeftExpanded }) {
                        Text(text = currentShapeValues.topStart.toString())
                    }
                    DropdownMenu(
                        modifier = Modifier.align(Alignment.TopStart),
                        expanded = isTopLeftExpanded,
                        onDismissRequest = { isTopLeftExpanded = false }) {
                        repeat(100) {
                            DropdownMenuItem(onClick = {
                                isTopLeftExpanded = false
                                currentShapeValues.topStart = it
                                onValueChange(currentShapeValues)
                            }) {
                                Text(text = it.toString())
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Button(onClick = { isTopRightExpanded = !isTopRightExpanded }) {
                        Text(text = currentShapeValues.topEnd.toString())
                    }
                    DropdownMenu(
                        expanded = isTopRightExpanded,
                        onDismissRequest = { isTopRightExpanded = false }) {
                        repeat(100) {
                            DropdownMenuItem(onClick = {
                                currentShapeValues = currentShapeValues.copy(topEnd = it)
                                onValueChange(currentShapeValues)
                            }) {
                                Text(text = it.toString())
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier.align(Alignment.BottomStart),
                ) {
                    Button(onClick = { isBottomLeftExpanded = !isBottomLeftExpanded }) {
                        Text(text = currentShapeValues.bottomStart.toString())
                    }
                    DropdownMenu(
                        expanded = isBottomLeftExpanded,
                        onDismissRequest = { isBottomLeftExpanded = false }) {
                        repeat(100) {
                            DropdownMenuItem(onClick = {
                                currentShapeValues = currentShapeValues.copy(bottomStart = it)
                                onValueChange(currentShapeValues)
                            }) {
                                Text(text = it.toString())
                            }
                        }
                    }
                }


                Box(
                    modifier = Modifier.align(Alignment.BottomEnd),
                ) {
                    Button(onClick = { isBottomRightExpanded = !isBottomRightExpanded }) {
                        Text(text = currentShapeValues.bottomEnd.toString())
                    }
                    DropdownMenu(
                        expanded = isBottomRightExpanded,
                        onDismissRequest = { isBottomRightExpanded = false }) {
                        repeat(100) {
                            DropdownMenuItem(onClick = {
                                currentShapeValues = currentShapeValues.copy(bottomEnd = it)
                                onValueChange(currentShapeValues)
                            }) {
                                Text(text = it.toString())
                            }
                        }
                    }
                }

            }
        }
    }
}