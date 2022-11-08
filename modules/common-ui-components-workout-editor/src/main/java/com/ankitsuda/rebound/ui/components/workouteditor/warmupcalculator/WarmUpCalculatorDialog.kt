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

package com.ankitsuda.rebound.ui.components.workouteditor.warmupcalculator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.base.util.lighterOrDarkerColor
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.rebound.ui.components.RButton
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.components.workouteditor.R
import com.ankitsuda.rebound.ui.keyboard.ReboundKeyboardHost
import com.ankitsuda.rebound.ui.keyboard.enums.ReboundKeyboardType
import com.ankitsuda.rebound.ui.keyboard.field.ReboundSetTextField
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WarmUpCalculatorDialog(
    startingWorkSetWeight: Double?,
    startingSets: List<WarmUpSet>,
    onInsert: (workSet: Double, sets: List<WarmUpSet>) -> Unit,
    onDismissRequest: () -> Unit,
    viewModel: WarmUpCalculatorDialogViewModel = hiltViewModel()
) {

    LaunchedEffect(startingSets) {
        viewModel.setSets(startingSets)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            ReboundKeyboardHost {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val sets by viewModel.sets.collectAsState()
                    WarmUpCalculatorDialogLayout(
                        sets = sets,
                        startingWorkSetWeight = startingWorkSetWeight,
                        onClickCancel = onDismissRequest,
                        onUpdateWorkSet = { barWeight, workSet ->
                            viewModel.updateWorkSet(barWeight, workSet)
                        },
                        onUpdateSet = {
                            viewModel.updateSet(it)
                        },
                        onAddSet = {
                            viewModel.addEmptySet(barWeight = it)
                        },
                        onClickInsert = {
                            onInsert(it, sets)
                            onDismissRequest()
                        },
                        onDeleteSet = {
                            viewModel.deleteSet(it)
                        }
                    )
                }
            }
        }
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WarmUpCalculatorDialogLayout(
    sets: List<WarmUpSet>,
    startingWorkSetWeight: Double?,
    onUpdateWorkSet: (barWight: Double, workSet: Double) -> Unit,
    onUpdateSet: (WarmUpSet) -> Unit,
    onDeleteSet: (WarmUpSet) -> Unit,
    onAddSet: (barWight: Double) -> Unit,
    onClickInsert: (workSet: Double) -> Unit,
    onClickCancel: () -> Unit
) {
    var workSetStr by remember {
        mutableStateOf(startingWorkSetWeight?.toReadableString() ?: "")
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        color = ReboundTheme.colors.background,
        shape = ReboundTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier =
                Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.warm_up_sets),
                    style = ReboundTheme.typography.h6,
                    color = ReboundTheme.colors.onBackground
                )
                RSpacer(space = 12.dp)

                Text(
                    text = stringResource(R.string.work_set_kg),
                    style = ReboundTheme.typography.caption,
                    color = ReboundTheme.colors.onBackground.copy(0.75f)
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                ReboundSetTextField(
                    bgColor = LocalElevationOverlay.current?.apply(
                        color = ReboundTheme.colors.background,
                        elevation = LocalAbsoluteElevation.current
                    )
                        ?: ReboundTheme.colors.background,
                    contentColor = ReboundTheme.colors.onBackground,
                    value = workSetStr,
                    onValueChange = {
                        workSetStr = it
                        onUpdateWorkSet(20.0, it.toDoubleOrNull() ?: 0.0)
                    },
                    reboundKeyboardType = ReboundKeyboardType.WEIGHT,
                )
            }

            RSpacer(space = 16.dp)

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentPadding = PaddingValues(top = 8.dp)
            ) {

                item(key = "warm_up_titles") {
                    WarmUpSetsTitlesComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                    )
                }

                items(items = sets, key = { it.id }) { set ->
                    WarmUpSetComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(),
                        workSetWeight = workSetStr.toDoubleOrNull() ?: 0.0,
                        barWeight = 20.0,
                        startingSet = set,
                        onChangeValue = onUpdateSet,
                        onDeleteSet = {
                            onDeleteSet(set)
                        }
                    )

                }

                item(key = "warm_up_add_set_button") {
                    RButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .animateItemPlacement(),
                        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = ReboundTheme.colors.background.lighterOrDarkerColor(
                                0.05f
                            ),
                            contentColor = ReboundTheme.colors.onBackground
                        ),
                        onClick = {
                            onAddSet(20.0)
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                        RSpacer(space = 8.dp)
                        Text(text = stringResource(R.string.add_set))
                    }
                }
            }

            DialogButtonsRow(
                onClickInsert = {
                    onClickInsert(workSetStr.toDoubleOrNull() ?: 0.0)
                },
                onClickCancel = onClickCancel
            )
        }
    }
}

@Composable
private fun ColumnScope.DialogButtonsRow(
    onClickInsert: () -> Unit,
    onClickCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .align(Alignment.End)
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 6.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onClickCancel) {
            Text("CANCEL")
        }
        TextButton(onClick = onClickInsert) {
            Text("INSERT")
        }
    }
}