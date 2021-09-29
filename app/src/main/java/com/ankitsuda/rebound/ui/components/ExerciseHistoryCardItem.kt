package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.data.entities.ExerciseLogEntry
import com.ankitsuda.rebound.data.entities.Workout
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

@Composable
fun ExerciseHistoryCardItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    workout: Workout,
    entries: List<ExerciseLogEntry>
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            workout.name?.let {
                Text(
                    text = it, style = ReboundTheme.typography.body1
                )
                RSpacer(4.dp)
            }

            workout.createdAt?.let {
                Text(
                    text = it.format(
                        DateTimeFormatter.ofLocalizedDateTime(
                            FormatStyle.MEDIUM,
                            FormatStyle.SHORT
                        )
                    ), style = ReboundTheme.typography.body1,
                    color = ReboundTheme.colors.onBackground.copy(alpha = 0.50f)
                )
                if (entries.isNotEmpty())
                    RSpacer(8.dp)
            }

            if ((workout.name != null || workout.createdAt != null) && entries.isNotEmpty()) RSpacer(
                space = 6.dp
            )

            if (entries.isNotEmpty()) {
                for (i in entries.indices) {
                    val entry = entries[i]
                    SessionExerciseSetItem(
                        order = i,
                        set = Pair(entry.weight ?: 0f, entry.reps ?: 0)
                    )
                    if (i != entries.size - 1) {
                        RSpacer(8.dp)
                    }
                }
            }
        }
    }
}