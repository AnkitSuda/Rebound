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
fun RoutineItemCard(
    modifier: Modifier = Modifier,
    name: String,
    date: String,
    totalExercises: Int,
    onClick: () -> Unit
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, style = MaterialTheme.typography.body1)
            Text(text = date, style = MaterialTheme.typography.body2)
            Text(
                text = "$totalExercises Exercises",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}