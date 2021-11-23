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
import androidx.compose.runtime.*
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
import com.ankitsuda.base.util.lighterOrDarkerColor
import com.ankitsuda.rebound.domain.entities.Exercise
import com.ankitsuda.rebound.domain.entities.ExerciseLogEntry
import com.ankitsuda.rebound.domain.entities.LogEntriesWithExerciseJunction
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.domain.ExerciseCategory

fun LazyListScope.WorkoutExerciseItemAlt(
    logEntriesWithJunction: LogEntriesWithExerciseJunction,
    onValuesUpdated: (updatedEntry: ExerciseLogEntry) -> Unit,
    onSwipeDelete: (ExerciseLogEntry) -> Unit,
    onAddSet: () -> Unit,
    onDeleteExercise: () -> Unit
) {

    val exercise = logEntriesWithJunction.exercise
    val logEntries = logEntriesWithJunction.logEntries


    // Exercise info
    item {
        val contentColor = ReboundTheme.colors.primary
        var popupMenuExpanded by remember {
            mutableStateOf(false)
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = exercise.name.toString(),
                style = ReboundTheme.typography.body1,
                fontSize = 18.sp,
                color = contentColor,
            )

            Column() {
                IconButton(onClick = {
                    popupMenuExpanded = true
                }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "More",
                        tint = contentColor
                    )
                }
                ExercisePopupMenu(
                    expanded = popupMenuExpanded,
                    onDismissRequest = { popupMenuExpanded = false },
                    onDeleteExercise = onDeleteExercise,
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
            if (exercise.category == ExerciseCategory.WEIGHTS_AND_REPS
                || exercise.category == ExerciseCategory.DISTANCE_AND_TIME
            ) {
                Text(
                    text = if (exercise.category == ExerciseCategory.WEIGHTS_AND_REPS) "KG" else "KM",
                    style = ReboundTheme.typography.caption,
                    color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1.25f)
                )
            }
            Text(
                text = if (exercise.category == ExerciseCategory.WEIGHTS_AND_REPS
                    || exercise.category == ExerciseCategory.REPS
                ) "Reps" else "Time",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.25f)
            )
            Box(
                modifier = Modifier.weight(0.5f),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Done,
                    contentDescription = null,
                    tint = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    // Sets
    items(items = logEntries, key = { it.entryId }) { entry ->
        SetItem(
            exercise = exercise,
            exerciseLogEntry = entry,
            onWeightChange = { _, value ->
                onValuesUpdated(entry.copy(weight = value))
            },
            onRepsChange = { _, value ->
                onValuesUpdated(entry.copy(reps = value))
            },
            onDistanceChange = { _, value ->
                onValuesUpdated(entry.copy(distance = value))
            },
            onTimeChange = { _, value ->
                onValuesUpdated(entry.copy(timeRecorded = value))
            },
            onCompleteChange = { _, value ->
                onValuesUpdated(entry.copy(completed = value))
            },
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
                backgroundColor = ReboundTheme.colors.background.lighterOrDarkerColor(0.05f),
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
    exercise: Exercise,
    exerciseLogEntry: ExerciseLogEntry,
    onWeightChange: (ExerciseLogEntry, Float?) -> Unit,
    onRepsChange: (ExerciseLogEntry, Int?) -> Unit,
    onDistanceChange: (ExerciseLogEntry, Long?) -> Unit,
    onTimeChange: (ExerciseLogEntry, Long?) -> Unit,
    onCompleteChange: (ExerciseLogEntry, Boolean) -> Unit,
    onSwipeDelete: (ExerciseLogEntry) -> Unit,
) {
    val bgColor by animateColorAsState(targetValue = if (exerciseLogEntry.completed) ReboundTheme.colors.primary else ReboundTheme.colors.background)
    val contentColor by animateColorAsState(targetValue = if (exerciseLogEntry.completed) ReboundTheme.colors.onPrimary else ReboundTheme.colors.onBackground)

    val dismissState = rememberDismissState(
        confirmStateChange = {
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
//            DismissDirection.StartToEnd,
            DismissDirection.EndToStart
        ),
        dismissThresholds = { direction ->
            FractionalThreshold(0.5f)
        },
        background = {
            val direction =
                dismissState.dismissDirection ?: DismissDirection.EndToStart

            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        if (direction == DismissDirection.EndToStart) ReboundTheme.colors.error.copy(
                            alpha = 0.10f
                        ) else bgColor
                    )
                    .padding(horizontal = 20.dp),
            ) {
                val alignment = Alignment.CenterEnd

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

            if (exercise.category == ExerciseCategory.DISTANCE_AND_TIME || exercise.category == ExerciseCategory.WEIGHTS_AND_REPS) {
                val fieldValue = if (exercise.category == ExerciseCategory.WEIGHTS_AND_REPS) {
                    (exerciseLogEntry.weight ?: "").toString()
                } else {
                    (exerciseLogEntry.distance ?: "").toString()
                }

                SetTextField(
                    value = fieldValue,
                    onValueChange = {
                        if (exercise.category == ExerciseCategory.WEIGHTS_AND_REPS) {
                            val newValue =
                                (if (it.isBlank()) null else it.trim()/*.filter { it.isDigit() }*/
                                    .toFloat())
                            onWeightChange(exerciseLogEntry, newValue)
                        } else {
                            val newValue =
                                (if (it.isBlank()) null else it.trim()/*.filter { it.isDigit() }*/
                                    .toLong())
                            onDistanceChange(exerciseLogEntry, newValue)
                        }
                    },
                    contentColor = contentColor,
                    bgColor = bgColor,
                )
            }

            val rightFieldValue =
                if (exercise.category == ExerciseCategory.WEIGHTS_AND_REPS || exercise.category == ExerciseCategory.REPS) {
                    (exerciseLogEntry.reps ?: "").toString()
                } else {
                    (exerciseLogEntry.timeRecorded ?: "").toString()
                }

            SetTextField(
                value = rightFieldValue,
                onValueChange = {
                    if (exercise.category == ExerciseCategory.WEIGHTS_AND_REPS || exercise.category == ExerciseCategory.REPS) {
                        val newValue =
                            (if (it.isBlank()) null else it.trim()/*.filter { it.isDigit() }*/
                                .toInt())
                        onRepsChange(exerciseLogEntry, newValue)
                    } else {
                        val newValue =
                            (if (it.isBlank()) null else it.trim()/*.filter { it.isDigit() }*/
                                .toLong())
                        onTimeChange(exerciseLogEntry, newValue)
                    }
                },
                contentColor = contentColor,
                bgColor = bgColor,
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

@Composable
fun ExercisePopupMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDeleteExercise: () -> Unit,
) {

    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(onClick = {
            onDismissRequest()
            onDeleteExercise()
        }) {
            Text("Delete exercise")
        }
    }
}