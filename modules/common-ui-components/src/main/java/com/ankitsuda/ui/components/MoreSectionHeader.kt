package com.ankitsuda.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoreSectionHeader(text: String) {
    Text(text = text, style = MaterialTheme.typography.caption, modifier = Modifier.padding(8.dp))
}

