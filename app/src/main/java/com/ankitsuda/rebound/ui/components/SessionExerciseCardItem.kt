package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun SessionExerciseCardItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    exerciseName: String,
    sets: List<Pair<Int, Int>>
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = exerciseName, style = ReboundTheme.typography.body1)
            AutoSpacer(8.dp)
            for (i in sets.indices) {
                val set = sets[i]
                SessionExerciseSetItem(order = i, set = set)
                if (i != sets.size - 1) {
                    AutoSpacer(8.dp)
                }
            }
        }
    }
}

@Composable
private fun SessionExerciseSetItem(order: Int, set: Pair<Int, Int>) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(224, 224, 224))
            ,
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = order.toString()
            )
        }

        AutoSpacer(16.dp)
        Text(text = buildAnnotatedString {
            append(set.first.toString())
            withStyle(style = SpanStyle(ReboundTheme.colors.onBackground.copy(alpha = 0.65f))) {
                append(" kg")
            }
        })
        AutoSpacer(20.dp)
        Text(text = buildAnnotatedString {
            append(set.second.toString())
            withStyle(style = SpanStyle(ReboundTheme.colors.onBackground.copy(alpha = 0.65f))) {
                append(" reps")
            }
        })
    }
}