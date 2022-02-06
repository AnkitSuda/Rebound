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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.*
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun AddPartMeasurementBottomSheet(
//    partId: Long? = null,
//    logId: Long? = null,
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: AddPartMeasurementBottomSheetViewModel = hiltViewModel()
) {
    val partId = try {
        navController.currentBackStackEntry?.arguments?.getString("partId")?.toLong()
    } catch (e: Exception) {
        null
    }
    val logId = try {
        navController.currentBackStackEntry?.arguments?.getString("logId")?.toLong()
    } catch (e: Exception) {
        null
    }

    val isUpdate = logId != null
    val fieldValue by viewModel.fieldValue.collectAsState("")
    val isCreateBtnEnabled = fieldValue.isNotBlank()

    LaunchedEffect(key1 = partId, key2 = logId) {
        viewModel.setFieldValue("")
    }

    if (isUpdate) {
        LaunchedEffect(key1 = logId) {
            viewModel.setLogId(logId!!)
        }
    }


    Column(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {

        TopBar(
            title = "$partId",
            statusBarEnabled = false,
            elevationEnabled = false
        )

        AppTextField(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
            value = fieldValue,
            placeholderValue = "",
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { viewModel.setFieldValue(it) }
        )

        Row(
            Modifier
                .padding(end = 16.dp, start = 16.dp, bottom = 16.dp)
                .align(Alignment.End)
        ) {
            if (isUpdate) {
                BottomSheetSecondaryRButton(
                    modifier = Modifier.padding(end = 16.dp),
                    onClick = {
                        viewModel.deleteMeasurementFromDb(logId!!)
                        navigator.goBack()
                    }) {
                    Text("Delete")
                }
            }

            BottomSheetRButton(
                enabled = isCreateBtnEnabled,
                onClick = {
                    if (isUpdate) {
                        viewModel.updateMeasurement()
                    } else {
                        partId?.let {
                            viewModel.addMeasurementToDb(partId)
                        }
                    }
                    navigator.goBack()
                },
                modifier = Modifier.width(88.dp)
            ) {
                Text(if (isUpdate) "Save" else "Add")
            }
        }
    }

}