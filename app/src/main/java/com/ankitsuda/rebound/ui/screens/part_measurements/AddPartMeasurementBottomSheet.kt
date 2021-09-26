package com.ankitsuda.rebound.ui.screens.part_measurements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.screens.main_screen.LocalBottomSheet
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun AddPartMeasurementBottomSheet(
    partId: Long? = null,
    logId: Long? = null,
    viewModel: AddPartMeasurementBottomSheetViewModel = hiltViewModel()
) {
    val bottomSheet = LocalBottomSheet.current
    val isUpdate = logId != null
    var fieldValue by remember {
        mutableStateOf("")
    }
    val isCreateBtnEnabled = fieldValue.isNotBlank()

    if (isUpdate) {
        LaunchedEffect(key1 = logId) {
            val log =viewModel.setLogId(logId!!)
            log?.measurement?.let {
                fieldValue = it.toString()
            }
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
            modifier = Modifier.padding(16.dp),
            value = fieldValue,
            placeholderValue = "",
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { fieldValue = it }
        )

        Row(
            Modifier
                .padding(end = 16.dp, start = 16.dp, bottom = 16.dp)
                .align(Alignment.End)
        ) {
            if (isUpdate) {
                TextButton(
                    modifier = Modifier.padding(end = 16.dp),
                    onClick = {
                        viewModel.deleteMeasurementFromDb(logId!!)
                        bottomSheet.hide()
                    }) {
                    Text("Delete")
                }
            }

            Button(
                enabled = isCreateBtnEnabled,
                onClick = {
                    partId?.let {
                        viewModel.addMeasurementToDb(fieldValue.toFloat(), partId)
                        bottomSheet.hide()
                    }
                }) {
                Text(if (isUpdate) "Save" else "Add")
            }
        }
    }

}