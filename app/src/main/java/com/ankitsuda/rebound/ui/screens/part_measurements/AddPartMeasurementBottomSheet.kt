package com.ankitsuda.rebound.ui.screens.part_measurements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.screens.main_screen.LocalBottomSheet
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun AddPartMeasurementBottomSheet(
    partId: Long,
    viewModel: AddPartMeasurementBottomSheetViewModel = hiltViewModel()
) {
    val bottomSheet = LocalBottomSheet.current

    val fieldValue by viewModel.fieldValue.collectAsState()
    val isCreateBtnEnabled = fieldValue.isNotBlank()

    Column(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {

        TopBar(
            title = "$partId",
            statusBarEnabled = false,
            elevationEnabled = false,
            leftIconBtn = {
                TopBarBackIconButton {
                    bottomSheet.hide()
                }
            },
            rightIconBtn = {
                TopBarIconButton(
                    icon = Icons.Outlined.Done,
                    title = "Create",
                    enabled = isCreateBtnEnabled,
                    customTint = MaterialTheme.colors.primary
                ) {
                    viewModel.addMeasurementToDb(fieldValue.toFloat(), partId)
                    bottomSheet.hide()
                }
            })

        AppTextField(
            modifier = Modifier.padding(16.dp),
            value = fieldValue,
            placeholderValue = "",
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { viewModel.setFieldValue(it) }
        )

    }

}