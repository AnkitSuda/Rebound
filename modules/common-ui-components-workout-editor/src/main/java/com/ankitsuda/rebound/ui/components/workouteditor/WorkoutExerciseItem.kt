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

package com.ankitsuda.rebound.ui.components.workouteditor

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.base.util.*
import com.ankitsuda.common.compose.*
import com.ankitsuda.rebound.domain.LogSetType
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.domain.entities.*
import com.ankitsuda.rebound.ui.components.RButton
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.components.workouteditor.components.SetFieldColumns
import com.ankitsuda.rebound.ui.components.workouteditor.components.SetFieldTitleColumns
import com.ankitsuda.rebound.ui.components.workouteditor.utils.WorkoutEditorUtils
import com.ankitsuda.rebound.ui.components.workouteditor.warmupcalculator.WarmUpCalculatorDialog
import com.ankitsuda.rebound.ui.components.workouteditor.warmupcalculator.WarmUpSet
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import java.util.*

private val ExerciseLogEntryComparator = Comparator<ExerciseLogEntry> { left, right ->
    left.setNumber?.compareTo(right.setNumber ?: 0) ?: 0
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.workoutExerciseItemAlt(
    useReboundKeyboard: Boolean = false,
    logEntriesWithJunction: LogEntriesWithExerciseJunction,
    barbells: List<Barbell>,
    onUpdateWarmUpSets: (List<WarmUpSet>) -> Unit,
    onValuesUpdated: (updatedEntry: ExerciseLogEntry) -> Unit,
    onSwipeDelete: (ExerciseLogEntry) -> Unit,
    onAddSet: () -> Unit,
    onDeleteExercise: () -> Unit,
    onAddEmptyNote: () -> Unit,
    onDeleteNote: (ExerciseSetGroupNote) -> Unit,
    onChangeNote: (ExerciseSetGroupNote) -> Unit,
    onAddToSuperset: () -> Unit,
    onRemoveFromSuperset: () -> Unit,
    onRequestBarbellChanger: () -> Unit,
) {

    val supersetId = logEntriesWithJunction.junction.supersetId
    val exercise = logEntriesWithJunction.exercise
    val logEntries = logEntriesWithJunction.logEntries
    val notes = logEntriesWithJunction.notes ?: emptyList()
    val sortedEntries = logEntries.sortedWith(ExerciseLogEntryComparator)
    val barbell = barbells.find { b -> b.id == logEntriesWithJunction.junction.barbellId }


    // Exercise info
    item(key = "${logEntriesWithJunction.junction.id}_exercise_info") {
        val contentColor = ReboundTheme.colors.primary
        var popupMenuExpanded by remember {
            mutableStateOf(false)
        }
        var warmUpSetsDialogVisible by rememberSaveable {
            mutableStateOf(false)
        }

        var dialogWarmUpSets by remember {
            mutableStateOf<List<WarmUpSet>>(emptyList())
        }
        var warmUpWorkSetWeight by rememberSaveable {
            mutableStateOf<Double?>(0.0)
        }

        fun updateWarmUpSets(newWarmUpWorkSetWeight: Double, newWarmUpSets: List<WarmUpSet>) {
            dialogWarmUpSets = newWarmUpSets
            warmUpWorkSetWeight = newWarmUpWorkSetWeight
            onUpdateWarmUpSets(newWarmUpSets)
        }

        LaunchedEffect(key1 = barbell) {
            if (dialogWarmUpSets.any { it.findFormula().contains("Bar") }) {
                dialogWarmUpSets = dialogWarmUpSets.map {
                    if (it.findFormula().contains("Bar")) {
                        it.copy(
                            weight = barbell.toWeightUnit(WeightUnit.KG)
                        )
                    } else {
                        it
                    }
                }
            }
        }

        // TODO: fix, sometimes does not get called
        LaunchedEffect(key1 = sortedEntries) {
            val newWorkWeight = (sortedEntries.filter { it.setType != LogSetType.WARM_UP }
                .getOrNull(0)?.weight ?: warmUpWorkSetWeight)

            val lastDialogWarmupSets = dialogWarmUpSets.toList()

            if (warmUpWorkSetWeight != newWorkWeight) {
                warmUpWorkSetWeight = newWorkWeight
                dialogWarmUpSets =
                    WarmUpSet.refreshWarmupSetsWithNewWorkWeight(
                        newWorkWeight,
                        lastDialogWarmupSets
                    )
            }
        }

        if (warmUpSetsDialogVisible) {
            key(LocalAppSettings.current.weightUnit) {
                WarmUpCalculatorDialog(
                    startingWorkSetWeight = warmUpWorkSetWeight,
                    barbell = barbell,
                    startingSets = dialogWarmUpSets,
                    onInsert = { newWarmUpWorkSetWeight, newWarmUpSets ->
                        updateWarmUpSets(newWarmUpWorkSetWeight, newWarmUpSets)
                    },
                    onDismissRequest = {
                        warmUpSetsDialogVisible = false
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp)
                .animateItemPlacement(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val supersetColor by remember(supersetId) {
                mutableStateOf(if (supersetId != null) colorFromSupersetId(supersetId) else null)
            }

            val titleBgColor by animateColorAsState(
                targetValue = supersetColor ?: ReboundTheme.colors.background
            )
            val titleColor by animateColorAsState(targetValue = if (supersetColor != null) if (supersetColor!!.isDark()) Color.White else Color.Black else ReboundTheme.colors.primary)

            Box(
                modifier = Modifier
                    .background(titleBgColor, ReboundTheme.shapes.small)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = exercise.name.toString(),
                    style = ReboundTheme.typography.body1,
                    fontSize = 18.sp,
                    color = titleColor,
                )
            }

            Column() {
                IconButton(onClick = {
                    popupMenuExpanded = true
                }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = stringResource(R.string.more),
                        tint = contentColor
                    )
                }
                ExercisePopupMenu(
                    isExpanded = popupMenuExpanded,
                    isInSuperset = logEntriesWithJunction.junction.supersetId != null,
                    onDismissRequest = { popupMenuExpanded = false },
                    onDeleteExercise = onDeleteExercise,
                    onAddWarmUpSets = {
                        warmUpSetsDialogVisible = true
                    },
                    onAddNote = onAddEmptyNote,
                    onAddToSuperset = onAddToSuperset,
                    onRemoveFromSuperset = onRemoveFromSuperset,
                    onChangeBarbell = onRequestBarbellChanger,
                )
            }

        }
    }

    items(items = notes, key = { it.id }) {
        SetGroupNoteComponent(
            modifier = Modifier.animateItemPlacement(),
            note = it,
            onDeleteNote = { onDeleteNote(it) },
            onChangeValue = { newValue ->
                onChangeNote(it.copy(note = newValue))
            }
        )
    }

    // Titles
    item(key = "${logEntriesWithJunction.junction.id}_titles") {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ReboundTheme.colors.background)
                .padding(start = 8.dp, end = 8.dp)
                .animateItemPlacement(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SetFieldTitleColumns(exercise = exercise)

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

    items(items = sortedEntries, key = {
        it.entryId
    }) { entry ->
        val appSettings = LocalAppSettings.current

        key(barbell, appSettings.weightUnit, appSettings.distanceUnit) {
            SetItem(
                useReboundKeyboard = useReboundKeyboard,
                revisedSetText = revisedSetsTexts[sortedEntries.indexOf(entry)],
                exercise = exercise,
                exerciseLogEntry = entry,
                barbell = barbell,
                onChange = {
                    onValuesUpdated(it)
                },
                onSwipeDelete = {
                    onSwipeDelete(it)
                },
            )
        }
    }

    // Add set button
    item(key = "${logEntriesWithJunction.junction.id}_add_set_button") {
        RButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                .animateItemPlacement(),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ReboundTheme.colors.background.lighterOrDarkerColor(0.05f),
                contentColor = ReboundTheme.colors.onBackground
            ),
            onClick = onAddSet
        ) {
            Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
            RSpacer(space = 8.dp)
            Text(text = stringResource(R.string.add_set))
        }
    }

    // Space
    item(key = "${logEntriesWithJunction.junction.id}_bottom_space") {
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.SetItem(
    exercise: Exercise,
    useReboundKeyboard: Boolean,
    barbell: Barbell?,
    revisedSetText: Pair<String, Color?>,
    exerciseLogEntry: ExerciseLogEntry,
    onChange: (ExerciseLogEntry) -> Unit,
    onSwipeDelete: (ExerciseLogEntry) -> Unit,
) {
    var mLogEntry by rememberSaveable {
        mutableStateOf(exerciseLogEntry)
    }

    val completionAnimDuration = 200
    val completionAnimSpecFloat =
        tween<Float>(
            durationMillis = completionAnimDuration,
            easing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)
        )
    val completionAnimSpecColor =
        tween<Color>(
            durationMillis = completionAnimDuration,
            easing = LinearEasing//CubicBezierEasing(0.5f, 1f, 0.89f, 1f)
        )

    val bgColor by animateColorAsState(
        targetValue = if (mLogEntry.completed) ReboundTheme.colors.primary else ReboundTheme.colors.background,
        animationSpec = completionAnimSpecColor
    )
    val contentColor by animateColorAsState(
        targetValue = if (mLogEntry.completed) ReboundTheme.colors.onPrimary else ReboundTheme.colors.onBackground,
        animationSpec = completionAnimSpecColor
    )

    var isScaleAnimRunning by rememberSaveable {
        mutableStateOf(false)
    }

    val scale by animateFloatAsState(
        targetValue = if (isScaleAnimRunning) 1.05f else 1f,
        animationSpec = completionAnimSpecFloat,
        finishedListener = {
            isScaleAnimRunning = false
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
        val isCompletable = WorkoutEditorUtils.isValidSet(
            logEntry = exerciseLogEntry,
            exerciseCategory = exercise.category
        )

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

    SetSwipeWrapperComponent(
        modifier = Modifier
            .scale(scale)
            .animateItemPlacement(),
        bgColor = bgColor,
        onSwipeDelete = {
            onSwipeDelete(exerciseLogEntry)
        }
    ) {
        SetItemLayout(
            bgColor = bgColor,
            useReboundKeyboard = useReboundKeyboard,
            contentColor = contentColor,
            exercise = exercise,
            exerciseLogEntry = mLogEntry,
            revisedSetText = revisedSetText,
            barbell = barbell,
            onWeightChange = { _, value ->
                handleOnChange(mLogEntry.copy(weight = value))
            },
            onRepsChange = { _, value ->
                handleOnChange(mLogEntry.copy(reps = value))
            },
            onDistanceChange = { _, value ->
                handleOnChange(mLogEntry.copy(distance = value))
            },
            onDurationChange = { _, value ->
                handleOnChange(mLogEntry.copy(timeRecorded = value))
            },
            onCompleteChange = { _, value ->
                handleOnCompleteChange(value)
            },
            onSetTypeChange = { _, value ->
                handleOnChange(mLogEntry.copy(setType = value))
            },
            onRpeChange = { _, value ->
                handleOnChange(mLogEntry.copy(rpe = value))
            },
        )
    }
}

@Composable
private fun SetItemLayout(
    bgColor: Color,
    useReboundKeyboard: Boolean,
    contentColor: Color,
    exercise: Exercise,
    exerciseLogEntry: ExerciseLogEntry,
    revisedSetText: Pair<String, Color?>,
    barbell: Barbell?,
    onWeightChange: (ExerciseLogEntry, Double?) -> Unit,
    onRepsChange: (ExerciseLogEntry, Int?) -> Unit,
    onDistanceChange: (ExerciseLogEntry, Double?) -> Unit,
    onDurationChange: (ExerciseLogEntry, Long?) -> Unit,
    onCompleteChange: (ExerciseLogEntry, Boolean) -> Unit,
    onSetTypeChange: (ExerciseLogEntry, LogSetType) -> Unit,
    onRpeChange: (ExerciseLogEntry, Float?) -> Unit,
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

        SetFieldColumns(
            contentColor = contentColor,
            bgColor = bgColor,
            exercise = exercise,
            barbell = barbell,
            exerciseLogEntry = exerciseLogEntry,
            onWeightChange = onWeightChange,
            onDistanceChange = onDistanceChange,
            onRepsChange = onRepsChange,
            onDurationChange = onDurationChange,
            onRpeChange = onRpeChange,
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
