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

package com.ankitsuda.rebound.ui.customizeplates.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.common.compose.localizedStr
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.customizeplates.R
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun PlateEditBottomSheet(
    navigator: Navigator = LocalNavigator.current,
    viewModel: PlateEditBottomSheetViewModel = hiltViewModel()
) {
    val weightFieldValue by viewModel.weightFieldValue.collectAsState()
    val colorFieldValue by viewModel.colorFieldValue.collectAsState()
    val heightFieldValue by viewModel.heightFieldValue.collectAsState()
    val widthFieldValue by viewModel.widthFieldValue.collectAsState()
    val weightUnitValue by viewModel.weightUnitValue.collectAsState()
    val isUpdate = viewModel.isUpdate

    PlateEditBottomSheetLayout(
        weightFieldValue = weightFieldValue,
        colorFieldValue = colorFieldValue,
        heightFieldValue = heightFieldValue,
        widthFieldValue = widthFieldValue,
        weightUnitValue = weightUnitValue,
        isUpdate = isUpdate,
        isSaveBtnEnabled = weightFieldValue.isNotBlank() &&
                colorFieldValue.isNotBlank() &&
                heightFieldValue.isNotBlank() &&
                widthFieldValue.isNotBlank(),
        onChangeWeightFieldValue = {
            viewModel.updateWeightFieldValue(it)
        },
        onChangeColorFieldValue = {
            viewModel.updateColorFieldValue(it)
        },
        onChangeHeightFieldValue = {
            viewModel.updateHeightFieldValue(it)
        },
        onChangeWidthFieldValue = {
            viewModel.updateWidthFieldValue(it)
        },
        onChangeWeightUnitValue = {
            viewModel.updateWeightUnitValue(it)
        },
        onDelete = {
            viewModel.deletePlate {
                navigator.goBack()
            }
        },
        onSave = {
            viewModel.savePlate {
                navigator.goBack()
            }
        }
    )
}

@Composable
private fun PlateEditBottomSheetLayout(
    weightFieldValue: String,
    colorFieldValue: String,
    heightFieldValue: String,
    widthFieldValue: String,
    weightUnitValue: WeightUnit,
    isUpdate: Boolean,
    isSaveBtnEnabled: Boolean,
    onChangeWeightFieldValue: (String) -> Unit,
    onChangeColorFieldValue: (String) -> Unit,
    onChangeHeightFieldValue: (String) -> Unit,
    onChangeWidthFieldValue: (String) -> Unit,
    onChangeWeightUnitValue: (WeightUnit) -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit
) {

    BottomSheetSurface {

        Column(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {

            TopBar(
                title = stringResource(R.string.plate),
                statusBarEnabled = false,
                elevationEnabled = false
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp,
                        top = 8.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                AppTextField(
                    modifier = Modifier,
                    value = weightFieldValue,
                    labelValue = stringResource(R.string.weight),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                    ),
                    onValueChange = onChangeWeightFieldValue
                )

                AppTextField(
                    modifier = Modifier,
                    value = colorFieldValue,
                    labelValue = stringResource(R.string.color),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                    onValueChange = onChangeColorFieldValue
                )

                AppTextField(
                    modifier = Modifier,
                    value = heightFieldValue,
                    labelValue = stringResource(R.string.height_multiplier),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                    ),
                    onValueChange = onChangeHeightFieldValue
                )
                AppTextField(
                    modifier = Modifier,
                    value = widthFieldValue,
                    labelValue = stringResource(R.string.width_multiplier),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                    ),
                    onValueChange = onChangeWidthFieldValue
                )
                FlowRow(
                    crossAxisSpacing = 8.dp,
                    mainAxisSpacing = 8.dp,
                    modifier = Modifier.padding(bottom = 12.dp, start = 16.dp, end = 16.dp)
                ) {
                    for (unit in WeightUnit.values()) {
                        Row(
                            modifier = Modifier.clickable(onClick = {
                                onChangeWeightUnitValue(unit)
                            }, indication = null,
                                interactionSource = remember { MutableInteractionSource() }),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = unit == weightUnitValue, onClick = {
                                onChangeWeightUnitValue(unit)
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                color = LocalThemeState.current.onBackgroundColor,
                                text = unit.localizedStr()
                            )
                        }
                    }
                }
            }

            Row(
                Modifier
                    .padding(end = 16.dp, start = 16.dp, bottom = 16.dp)
                    .align(Alignment.End)
            ) {
                BottomSheetSecondaryRButton(
                    modifier = Modifier.padding(end = 16.dp),
                    onClick = {
                        onDelete()
                    }) {
                    Text(if (isUpdate) stringResource(R.string.delete) else stringResource(R.string.cancel))
                }


                BottomSheetRButton(
                    enabled = isSaveBtnEnabled,
                    onClick = {
                        onSave()
                    },
                    modifier = Modifier.width(88.dp)
                ) {
                    Text(if (isUpdate) stringResource(R.string.save) else stringResource(R.string.add))
                }

            }
        }
    }
}