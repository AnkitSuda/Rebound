package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeekDay(modifier: Modifier, day: Date, isSelected: Boolean, onClick: () -> Unit) {
    Box(modifier = modifier.clickable(onClick = onClick)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val dayDate = SimpleDateFormat("d").format(day)
            val dayD = SimpleDateFormat("EEE").format(day).uppercase()

            Text(
                text = dayD,
                style = MaterialTheme.typography.body2,
                color = if (isSelected) MaterialTheme.colors.primary else Color(117, 117, 117)
            )
            Text(
                text = dayDate,
                style = MaterialTheme.typography.body1,
                color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}