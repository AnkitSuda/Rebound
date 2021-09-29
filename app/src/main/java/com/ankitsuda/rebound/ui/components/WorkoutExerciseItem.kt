package com.ankitsuda.rebound.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.rebound.data.entities.ExerciseLogEntry
import com.ankitsuda.rebound.data.entities.LogEntriesWithExerciseJunction
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.utils.lighterOrDarkerColor
import timber.log.Timber

fun LazyListScope.WorkoutExerciseItemAlt(
    logEntriesWithJunction: LogEntriesWithExerciseJunction,
    onWeightChange: (ExerciseLogEntry, Float) -> Unit,
    onRepsChange: (ExerciseLogEntry, Int) -> Unit,
    onCompleteChange: (ExerciseLogEntry, Boolean) -> Unit,
    onSwipeDelete: (ExerciseLogEntry) -> Unit,
    onAddSet: () -> Unit,
) {

    val exercise = logEntriesWithJunction.exercise
    val logEntries = logEntriesWithJunction.logEntries

    // Exercise info
    item {
        val contentColor = ReboundTheme.colors.primary
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = exercise.name.toString(),
                style = ReboundTheme.typography.body2,
                color = contentColor,
            )

            IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "More",
                    tint = contentColor
                )
            }
        }
    }

    // Titles
    item {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "SET",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.5f)
            )
            Text(
                text = "PREVIOUS",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.5f)
            )
            Text(
                text = "KG",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.25f)
            )
            Text(
                text = "REPS",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.25f)
            )
            Icon(
                imageVector = Icons.Outlined.Done,
                contentDescription = null,
                tint = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                modifier = Modifier.weight(0.5f)
            )
        }
    }

    // Sets
    items(items = logEntries, key = { it.entryId }) { entry ->
        SetItem(
            exerciseLogEntry = entry,
            onWeightChange = onWeightChange,
            onRepsChange = onRepsChange,
            onCompleteChange = onCompleteChange,
            onSwipeDelete = onSwipeDelete
        )
    }

    // Add set button
    item {
        RButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ReboundTheme.colors.background.lighterOrDarkerColor(
                    0.05f
                ),
                contentColor = ReboundTheme.colors.onBackground
            ),
            onClick = onAddSet
        ) {
            Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
            RSpacer(space = 8.dp)
            Text(text = "Add set")
        }
    }

    // Space
    item {
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SetItem(
    exerciseLogEntry: ExerciseLogEntry,
    onWeightChange: (ExerciseLogEntry, Float) -> Unit,
    onRepsChange: (ExerciseLogEntry, Int) -> Unit,
    onCompleteChange: (ExerciseLogEntry, Boolean) -> Unit,
    onSwipeDelete: (ExerciseLogEntry) -> Unit,
) {
    val bgColor by animateColorAsState(targetValue = if (exerciseLogEntry.completed) ReboundTheme.colors.primary else ReboundTheme.colors.background)
    val contentColor by animateColorAsState(targetValue = if (exerciseLogEntry.completed) ReboundTheme.colors.onPrimary else ReboundTheme.colors.onBackground)

    val dismissState = rememberDismissState(
        confirmStateChange = {
            Timber.d("dismiss value ${it.name}")
//            if (it == DismissValue.DismissedToEnd) isDeleted =
//                !isDeleted
//            else if (it == DismissValue.DismissedToStart) isDeleted =
//                !isDeleted
//            it != DismissValue.DismissedToStart || it != DismissValue.DismissedToEnd
            if (it != DismissValue.Default) {
                onSwipeDelete(exerciseLogEntry)
                false
            } else {
                true
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(
            DismissDirection.StartToEnd,
            DismissDirection.EndToStart
        ),
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
        },
        background = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(ReboundTheme.colors.error.copy(alpha = 0.10f))
                    .padding(horizontal = 20.dp),
            ) {

                val direction =
                    dismissState.dismissDirection ?: DismissDirection.StartToEnd

                val alignment = when (direction) {
                    DismissDirection.StartToEnd -> Alignment.CenterStart
                    DismissDirection.EndToStart -> Alignment.CenterEnd
                }

                val scale by animateFloatAsState(
                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                )

                Icon(
                    Icons.Outlined.Delete,
                    tint = ReboundTheme.colors.error,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .scale(scale)
                        .align(alignment)
                )

            }
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = bgColor)
                .padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = (exerciseLogEntry.setNumber ?: 0).toString(),
                style = ReboundTheme.typography.caption,
                color = contentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.5f)
            )
            Text(
                text = "2.5 kg x 12",
                style = ReboundTheme.typography.caption,
                textAlign = TextAlign.Center,
                color = contentColor,
                modifier = Modifier.weight(1.5f)
            )

            SetTextField(
                value = (exerciseLogEntry.weight ?: 0f).toString(),
                onValueChange = {
                    val newValue =
                        (if (it.isBlank()) "0" else it.trim()/*.filter { it.isDigit() }*/).toFloat()
                    onWeightChange(exerciseLogEntry, newValue)
                },
                contentColor = contentColor,
                bgColor = bgColor,
            )
            SetTextField(
                value = (exerciseLogEntry.reps ?: 0).toString(),
                onValueChange = {
                    val newValue =
                        (if (it.isBlank()) "0" else it.trim()/*.filter { it.isDigit() }*/).toInt()
                    onRepsChange(exerciseLogEntry, newValue)
                },
                contentColor = contentColor,
                bgColor = bgColor
            )

            IconButton(
                onClick = {
                    onCompleteChange(
                        exerciseLogEntry,
                        !exerciseLogEntry.completed
                    )
                },
                modifier = Modifier.weight(0.5f)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(
                            shape = CircleShape,
                            color = bgColor.lighterOrDarkerColor(0.05f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Done,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun RowScope.SetTextField(
    value: String,
    onValueChange: (String) -> Unit,
    contentColor: Color,
    bgColor: Color
) {
    BasicTextField(
        modifier = Modifier
//            .width(64.dp)
            .height(32.dp)
            .padding(start = 8.dp, end = 8.dp)
            .weight(1.25f)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor.lighterOrDarkerColor(0.05f)),
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = contentColor
        ),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
    )
}