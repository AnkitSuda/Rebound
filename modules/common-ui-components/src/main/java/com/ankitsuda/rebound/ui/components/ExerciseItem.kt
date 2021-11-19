package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseItem(
    modifier: Modifier = Modifier, name: String, muscle: String, totalLogs: Int, onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = name, style = MaterialTheme.typography.body1)
                Text(
                    text = muscle,
                    style = MaterialTheme.typography.caption,
                    color = Color(158, 158, 158)
                )
            }
            if (totalLogs > 0) {
                Text(
                    text = totalLogs.toString(),
                    style = MaterialTheme.typography.subtitle2,
                    color = Color(158, 158, 158)
                )
            }

        }
    }
}