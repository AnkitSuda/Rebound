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

package com.ankitsuda.rebound.ui.measure.part.add_sheet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.domain.entities.BodyPart
import com.ankitsuda.rebound.domain.entities.BodyPartUnitType
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.measure.part.R
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun AddPartMeasurementBottomSheet(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: AddPartMeasurementBottomSheetViewModel = hiltViewModel()
) {
    val isUpdate by viewModel.isUpdate.collectAsState(false)
    val fieldValue by viewModel.fieldValue.collectAsState("")
    val bodyPart by viewModel.bodyPart.collectAsState(initial = BodyPart(id = ""))
    val isCreateBtnEnabled = fieldValue.isNotBlank()

    BottomSheetSurface {
        Column(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {

            TopBar(
                title = stringResource(id = R.string.measurement),
                statusBarEnabled = false,
                elevationEnabled = false
            )

            Box(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                    top = 8.dp
                )
            ) {
                AppTextField(
                    value = fieldValue,
                    placeholderValue = "",
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                    ),
                    onValueChange = { viewModel.setFieldValue(it) }
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp),
                    text = when (bodyPart.unitType ?: BodyPartUnitType.LENGTH) {
                        BodyPartUnitType.WEIGHT -> stringResource(id = R.string.kg)
                        BodyPartUnitType.CALORIES -> stringResource(id = R.string.kcal)
                        BodyPartUnitType.PERCENTAGE -> "%"
                        BodyPartUnitType.LENGTH -> stringResource(id = R.string.inch_short)
                        else -> ""
                    },
                    style = ReboundTheme.typography.caption,
                    color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f)
                )
            }

            Row(
                Modifier
                    .padding(end = 16.dp, start = 16.dp, bottom = 16.dp)
                    .align(Alignment.End)
            ) {
                if (isUpdate) {
                    BottomSheetSecondaryRButton(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = {
                            viewModel.deleteMeasurementFromDb()
                            navigator.goBack()
                        }) {
                        Text(stringResource(id = R.string.delete))
                    }
                }

                BottomSheetRButton(
                    enabled = isCreateBtnEnabled,
                    onClick = {
                        viewModel.saveMeasurement()
                        navigator.goBack()
                    },
                    modifier = Modifier.width(88.dp)
                ) {
                    Text(if (isUpdate) stringResource(id = R.string.save) else stringResource(id = R.string.add))
                }
            }
        }
    }
}