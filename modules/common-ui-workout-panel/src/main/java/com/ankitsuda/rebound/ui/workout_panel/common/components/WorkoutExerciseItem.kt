/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.ankitsuda.rebound.ui.workout_panel.common.components

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.ankitsuda.rebound.domain.LogSetType
import com.ankitsuda.rebound.ui.components.RButton
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

private val ExerciseLogEntryComparator = Comparator<ExerciseLogEntry> { left, right ->
    left.setNumber?.compareTo(right.setNumber ?: 0) ?: 0
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.workoutExerciseItemAlt(
    logEntriesWithJunction: LogEntriesWithExerciseJunction,
    onValuesUpdated: (updatedEntry: ExerciseLogEntry) -> Unit,
    onSwipeDelete: (ExerciseLogEntry) -> Unit,
    onAddSet: () -> Unit,
    onDeleteExercise: () -> Unit
) {

    val exercise = logEntriesWithJunction.exercise
    val logEntries = logEntriesWithJunction.logEntries


    // Exercise info
    item() {
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
//            Text(
//                text = "PREVIOUS",
//                style = ReboundTheme.typography.caption,
//                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
//                textAlign = TextAlign.Center,
//                modifier = Modifier.weight(1.5f)
//            )
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
    val sortedEntries = logEntries.sortedWith(ExerciseLogEntryComparator)


    fun getRevisedSetNumbers(): List<Pair<String, Color?>> {
        var counter = 0
        val newPairs = sortedEntries.map {
            when (it.setType ?: LogSetType.NORMAL) {
                LogSetType.NORMAL -> {
                    counter++
                    Pair(counter.toString(), null)
                }
                LogSetType.WARM_UP -> Pair("W", Color.Yellow)
                LogSetType.DROP_SET -> {
                    counter++
                    Pair("D", Color.Magenta)
                }
                LogSetType.FAILURE -> {
                    counter++
                    Pair("F", Color.Red)
                }
            }
        }

        return newPairs
    }

    val revisedSetsTexts = getRevisedSetNumbers()

    items(items = sortedEntries, key = { "${it.entryId}_${it.setNumber}" }) { entry ->


        SetItem(
            revisedSetText = revisedSetsTexts[sortedEntries.indexOf(entry)],
            exercise = exercise,
            exerciseLogEntry = entry,
            onChange = {
                onValuesUpdated(it)
            },
            onSwipeDelete = {
                onSwipeDelete(it)
            }
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.SetItem(
    exercise: Exercise,
    revisedSetText: Pair<String, Color?>,
    exerciseLogEntry: ExerciseLogEntry,
    onChange: (ExerciseLogEntry) -> Unit,
    onSwipeDelete: (ExerciseLogEntry) -> Unit,
) {
    var mLogEntry by rememberSaveable {
        mutableStateOf(exerciseLogEntry)
    }

    val bgColor by animateColorAsState(
        targetValue = if (mLogEntry.completed) ReboundTheme.colors.primary else ReboundTheme.colors.background,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val contentColor by animateColorAsState(
        targetValue = if (mLogEntry.completed) ReboundTheme.colors.onPrimary else ReboundTheme.colors.onBackground,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    var isScaleAnimRunning by rememberSaveable {
        mutableStateOf(false)
    }

    val scale by animateFloatAsState(
        targetValue = if (isScaleAnimRunning) 1.05f else 1f,
//        animationSpec = tween(durationMillis = 300),
        finishedListener = {
            isScaleAnimRunning = false
        }
    )

    val coroutine = rememberCoroutineScope()

    fun handleOnSwiped() {
        coroutine.launch {
            delay(125)
            onSwipeDelete(exerciseLogEntry)
        }
    }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it != DismissValue.Default) {
                handleOnSwiped()
                true
            } else {
                false
            }
        }
    )

    fun handleOnChange(updatedEntry: ExerciseLogEntry) {
        if (updatedEntry.completed) {
            isScaleAnimRunning = false
        }
        updatedEntry.completed = false
        mLogEntry = updatedEntry
        onChange(updatedEntry)
    }

    fun handleOnCompleteChange(isComplete: Boolean) {
        val isCompletable = when (exercise.category) {
            ExerciseCategory.DISTANCE_AND_TIME -> {
                mLogEntry.distance != null && mLogEntry.timeRecorded != null
            }
            ExerciseCategory.WEIGHTS_AND_REPS -> {
                mLogEntry.weight != null && mLogEntry.reps != null
            }
            ExerciseCategory.REPS -> {
                mLogEntry.reps != null
            }
            ExerciseCategory.TIME -> {
                mLogEntry.timeRecorded != null
            }
            ExerciseCategory.UNKNOWN -> {
                true
            }
        }

        if (isComplete && isCompletable) {
            isScaleAnimRunning = true
        }

        val updatedEntry =
            mLogEntry.copy(
                completed = if (isComplete && isCompletable) {
                    true
                } else if (!isComplete) {
                    isComplete
                } else {
                    false
                }
            )
        mLogEntry = updatedEntry
        onChange(updatedEntry)
    }

    SwipeToDismiss(
        modifier = Modifier
            .scale(scale),
        state = dismissState,
        directions = setOf(
//            DismissDirection.StartToEnd,
            DismissDirection.EndToStart
        ),
        dismissThresholds = {
            FractionalThreshold(0.5f)
        },
        background = {
            SetItemBgLayout(
                bgColor = bgColor,
                dismissState = dismissState
            )
        },
    ) {
        SetItemLayout(
            bgColor = bgColor,
            contentColor = contentColor,
            exercise = exercise,
            exerciseLogEntry = mLogEntry,
            revisedSetText = revisedSetText,
            onWeightChange = { _, value ->
                handleOnChange(mLogEntry.copy(weight = value))
            },
            onRepsChange = { _, value ->
                handleOnChange(mLogEntry.copy(reps = value))
            },
            onDistanceChange = { _, value ->
                handleOnChange(mLogEntry.copy(distance = value))
            },
            onTimeChange = { _, value ->
                handleOnChange(mLogEntry.copy(timeRecorded = value))
            },
            onCompleteChange = { _, value ->
                handleOnCompleteChange(value)
            },
            onSetTypeChange = { _, value ->
                handleOnChange(mLogEntry.copy(setType = value))
            },
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SetItemBgLayout(
    bgColor: Color,
    dismissState: DismissState
) {
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
}

@Composable
private fun SetItemLayout(
    bgColor: Color,
    contentColor: Color,
    exercise: Exercise,
    exerciseLogEntry: ExerciseLogEntry,
    revisedSetText: Pair<String, Color?>,
    onWeightChange: (ExerciseLogEntry, Float?) -> Unit,
    onRepsChange: (ExerciseLogEntry, Int?) -> Unit,
    onDistanceChange: (ExerciseLogEntry, Long?) -> Unit,
    onTimeChange: (ExerciseLogEntry, Long?) -> Unit,
    onCompleteChange: (ExerciseLogEntry, Boolean) -> Unit,
    onSetTypeChange: (ExerciseLogEntry, LogSetType) -> Unit,
) {
    val typeOfSet = exerciseLogEntry.setType ?: LogSetType.NORMAL
    var isSetTypeChangerExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = bgColor)
            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier
                .weight(0.5f)
                .clickable {
                    isSetTypeChangerExpanded = true
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = revisedSetText.first,
                style = ReboundTheme.typography.caption,
                color = revisedSetText.second ?: contentColor,
                textAlign = TextAlign.Center,
            )
            SetTypeChangerMenu(
                selectedType = typeOfSet,
                expanded = isSetTypeChangerExpanded,
                onDismissRequest = {
                    isSetTypeChangerExpanded = false
                },
                onChangeSetType = {
                    isSetTypeChangerExpanded = false
                    onSetTypeChange(exerciseLogEntry, it)
                }
            )
        }
//        Text(
//            text = "2.5 kg x 12",
//            style = ReboundTheme.typography.caption,
//            textAlign = TextAlign.Center,
//            color = contentColor,
//            modifier = Modifier.weight(1.5f)
//        )

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