package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.theme.ReboundTheme

/**
 * Dummy content
 */
@Composable
fun HistorySessionItemCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    totalExercises: Int,
    time: String,
    volume: String,
    prs: Int,
) {

    AppCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = ReboundTheme.typography.body1)
            Spacer(Modifier.height(4.dp))
            Text(text = "$totalExercises Exercises", style = ReboundTheme.typography.caption)
            Spacer(Modifier.height(4.dp))
            SessionCompleteQuickInfo(time = time, volume = volume, prs = prs)
        }
    }
}