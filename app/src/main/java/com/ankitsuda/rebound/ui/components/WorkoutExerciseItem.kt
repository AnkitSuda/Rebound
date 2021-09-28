package com.ankitsuda.rebound.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.data.entities.ExerciseLogEntry
import com.ankitsuda.rebound.ui.theme.ReboundTheme


@Composable
fun WorkoutExerciseItem(
    modifier: Modifier = Modifier,
    exerciseLogEntries: List<ExerciseLogEntry>,
    onWeightChange: (Float) -> Unit,
    onRepsChange: (Int) -> Unit,
    onCompleteChange: (Boolean) -> Unit,
    onSwipeDelete: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Name",
                style = ReboundTheme.typography.body2,
                color = ReboundTheme.colors.primary
            )

            IconButton(onClick = {

            }) {
                Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "More")
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "SET",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f)
            )
            Text(
                text = "PREVIOUS",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f)
            )
            Text(
                text = "KG",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f)
            )
            Text(
                text = "REPS",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f)
            )
            Icon(
                imageVector = Icons.Outlined.Done,
                contentDescription = null,
                tint = ReboundTheme.colors.onBackground.copy(alpha = 0.5f)
            )
        }

        for (entry in exerciseLogEntries) {
            SetItem(
                number = entry.setNumber,
                previous = "2.5 kg x 12",
                weight = entry.weight ?: 0f,
                reps = entry.reps ?: 0,
                completed = false,
                onWeightChange = onWeightChange,
                onRepsChange = onRepsChange,
                onCompleteChange = onCompleteChange,
                onSwipeDelete = onSwipeDelete
            )
        }
    }
}

@Composable
private fun SetItem(
    number: Int,
    previous: String,
    weight: Float,
    reps: Int,
    completed: Boolean,
    onWeightChange: (Float) -> Unit,
    onRepsChange: (Int) -> Unit,
    onCompleteChange: (Boolean) -> Unit,
    onSwipeDelete: () -> Unit
) {
    val bgColor by animateColorAsState(targetValue = if (completed) ReboundTheme.colors.primary else ReboundTheme.colors.background)
    val contentColor by animateColorAsState(targetValue = if (completed) ReboundTheme.colors.onPrimary else ReboundTheme.colors.onBackground)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .background(color = bgColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "$number",
            style = ReboundTheme.typography.caption,
            color = contentColor,
        )
        Text(
            text = previous,
            style = ReboundTheme.typography.caption,
            color = contentColor,
        )
        TextField(
            modifier = Modifier.width(64.dp),
            value = "$weight",
            onValueChange = {
                onWeightChange(it.toFloat())
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                textColor = contentColor,
                backgroundColor = if (completed) ReboundTheme.colors.primary else ReboundTheme.colors.card
            )
        )
        TextField(
            modifier = Modifier.width(64.dp),
            value = "$reps",
            onValueChange = {
                onRepsChange(it.toInt())
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                textColor = contentColor,
                backgroundColor = if (completed) ReboundTheme.colors.primary else ReboundTheme.colors.card
            )
        )
        IconButton(onClick = { onCompleteChange(!completed) }) {
            Icon(
                imageVector = Icons.Outlined.Done,
                contentDescription = null,
                tint = contentColor
            )
        }
    }

}