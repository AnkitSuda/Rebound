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

package com.ankitsuda.rebound.ui.customizebarbells.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.customizebarbells.R

@Composable
fun BarbellEditBottomSheet(
    navigator: Navigator = LocalNavigator.current,
    viewModel: BarbellEditBottomSheetViewModel = hiltViewModel()
) {
    val nameFieldValue by viewModel.nameFieldValue.collectAsState()
    val weightKgFieldValue by viewModel.weightKgFieldValue.collectAsState()
    val weightLbsFieldValue by viewModel.weightLbsFieldValue.collectAsState()
    val isUpdate = viewModel.isUpdate

    BarbellEditBottomSheetLayout(
        nameFieldValue = nameFieldValue,
        weightKgFieldValue = weightKgFieldValue,
        weightLbsFieldValue = weightLbsFieldValue,
        isUpdate = isUpdate,
        isSaveBtnEnabled = nameFieldValue.isNotBlank() &&
                weightKgFieldValue.isNotBlank() &&
                weightLbsFieldValue.isNotBlank(),
        onChangeNameFieldValue = {
            viewModel.updateNameFieldValue(it)
        },
        onChangeWeightKgFieldValue = {
            viewModel.updateWeightKgFieldValue(it)
        },
        onChangeWeightLbsFieldValue = {
            viewModel.updateWeightLbsFieldValue(it)
        },
        onDelete = {
            viewModel.deleteBarbell {
                navigator.goBack()
            }
        },
        onSave = {
            viewModel.saveBarbell {
                navigator.goBack()
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BarbellEditBottomSheetLayout(
    nameFieldValue: String,
    weightKgFieldValue: String,
    weightLbsFieldValue: String,
    isUpdate: Boolean,
    isSaveBtnEnabled: Boolean,
    onChangeNameFieldValue: (String) -> Unit,
    onChangeWeightKgFieldValue: (String) -> Unit,
    onChangeWeightLbsFieldValue: (String) -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit
) {

    val keyboard = LocalSoftwareKeyboardController.current

    BottomSheetSurface {

        Column(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {

            TopBar(
                title = stringResource(R.string.barbell),
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
                    value = nameFieldValue,
                    labelValue = stringResource(R.string.name),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    onValueChange = onChangeNameFieldValue
                )
                AppTextField(
                    modifier = Modifier,
                    value = weightKgFieldValue,
                    labelValue = stringResource(R.string.weight_kg),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                    onValueChange = onChangeWeightKgFieldValue
                )
                AppTextField(
                    modifier = Modifier,
                    value = weightLbsFieldValue,
                    labelValue = stringResource(R.string.weight_lbs),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isSaveBtnEnabled) {
                                onSave()
                            } else {
                                keyboard?.hide()
                            }
                        }
                    ),
                    onValueChange = onChangeWeightLbsFieldValue
                )
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