package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun ExerciseHistoryCardItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    date: String,
    sets: List<Pair<Int, Int>>
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = date, style = ReboundTheme.typography.body1)
            RSpacer(8.dp)
            for (i in sets.indices) {
                val set = sets[i]
                SessionExerciseSetItem(order = i, set = set)
                if (i != sets.size - 1) {
                    RSpacer(8.dp)
                }
            }
        }
    }
}