package com.ankitsuda.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TemplateItemCard(
    modifier: Modifier = Modifier,
    name: String,
    totalExercises: Int,
    onClick: () -> Unit
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, style = MaterialTheme.typography.body1)
            Text(
                text = "$totalExercises Exercises",
                style = MaterialTheme.typography.body2,
            )
        }
    }
}