package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ankitsuda.ui.theme.ReboundTheme
import com.ankitsuda.rebound.utils.isDark
import com.ankitsuda.rebound.utils.lighterOrDarkerColor

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholderValue: String,
    backgroundColor: Color = ReboundTheme.colors.background.lighterOrDarkerColor(0.1f),
    singleLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = if (singleLine) 1 else Int.MAX_VALUE,
        colors = TextFieldDefaults.textFieldColors(
            textColor = if (backgroundColor.isDark()) Color.White else Color.Black,
            disabledTextColor = Color.Transparent,
            backgroundColor = backgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        shape = MaterialTheme.shapes.medium,
        placeholder = { Text(text = placeholderValue) }
    )

}