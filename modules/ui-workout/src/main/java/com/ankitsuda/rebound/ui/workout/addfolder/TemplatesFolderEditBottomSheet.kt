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

package com.ankitsuda.rebound.ui.workout.addfolder

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.*

@Composable
fun TemplatesFolderEditBottomSheet(
    navigator: Navigator = LocalNavigator.current,
    viewModel: TemplatesFolderEditBottomSheetViewModel = hiltViewModel()
) {
    val isUpdate by viewModel.isUpdate.collectAsState(false)
    val fieldValue by viewModel.fieldValue.collectAsState("")
    val isCreateBtnEnabled = fieldValue.isNotBlank()

    BottomSheetSurface {
        Column(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {

            TopBar(
                title = "Folder",
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
                    onValueChange = { viewModel.setFieldValue(it) }
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
                            viewModel.deleteFolderFromDb()
                            navigator.goBack()
                        }) {
                        Text("Delete")
                    }
                }

                BottomSheetRButton(
                    enabled = isCreateBtnEnabled,
                    onClick = {
                        viewModel.saveFolder()
                        navigator.goBack()
                    },
                    modifier = Modifier.width(88.dp)
                ) {
                    Text(if (isUpdate) "Save" else "Add")
                }
            }
        }
    }
}