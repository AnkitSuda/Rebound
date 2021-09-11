package com.ankitsuda.rebound.ui.screens.create_exercise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.AppTextField
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import kotlin.random.Random

@Composable
fun CreateExerciseScreen(
    navController: NavHostController,
    viewModel: CreateExerciseScreenViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    // Dummy lists
    val categoriesList = arrayListOf("Weights & Reps", "Reps", "Distance & Time", "Time")
    val musclesList = arrayListOf<String>().apply {
        repeat(10) {
            add("Muscle")
        }
    }

    val nameValue by viewModel.name.observeAsState("")
    val noteValue by viewModel.note.observeAsState("")

    val isCreateBtnEnabled = nameValue.trim().isNotEmpty()



    Column() {
        TopBar(title = "New Exercise", leftIconBtn = {
            TopBarBackIconButton {
                navController.popBackStack()
            }
        }, rightIconBtn = {
            TopBarIconButton(
                icon = Icons.Outlined.Done,
                title = "Create",
                enabled = isCreateBtnEnabled,
                tint = MaterialTheme.colors.primary
            ) {

            }
        })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.caption,
                    color = Color(117, 117, 117)
                )
                Spacer(modifier = Modifier.height(8.dp))
                AppTextField(
                    value = nameValue,
                    placeholderValue = "Exercise name",
                    singleLine = true,
                    onValueChange = {
                        viewModel.setName(it)
                    })
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {

                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.caption,
                    color = Color(117, 117, 117)
                )
                Spacer(modifier = Modifier.height(8.dp))
                AppTextField(
                    value = noteValue,
                    placeholderValue = "Exercise notes",
                    onValueChange = {
                        viewModel.setNote(it)
                    })
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)

            ) {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.caption,
                    color = Color(117, 117, 117)
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(crossAxisSpacing = 8.dp) {
                    for (category in categoriesList) {
                        Row(modifier = Modifier.width((LocalConfiguration.current.screenWidthDp / 2.5).dp)) {
                            RadioButton(selected = false, onClick = {
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = category)
                        }
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)

            ) {
                Text(
                    text = "Primary Muscle",
                    style = MaterialTheme.typography.caption,
                    color = Color(117, 117, 117)
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(crossAxisSpacing = 8.dp) {
                    for (muscle in musclesList) {
                        Row(modifier = Modifier.width((LocalConfiguration.current.screenWidthDp / 2.5).dp)) {
                            RadioButton(selected = false, onClick = {
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = muscle)
                        }
                    }
                }
            }
        }
    }
}