package com.ankitsuda.rebound.ui.screens.create_exercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.ui.components.*
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import timber.log.Timber
import kotlin.random.Random

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun CreateExerciseScreen(
    navigator: Navigator = LocalNavigator.current,
    viewModel: CreateExerciseScreenViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    // Dummy lists
    val categoriesList = viewModel.allCategories
    val musclesList by viewModel.allPrimaryMuscles.collectAsState(initial = emptyList())

    val nameValue by viewModel.name.observeAsState("")
    val noteValue by viewModel.note.observeAsState("")

    val selectedCategory by viewModel.selectedCategory.observeAsState("Weights & Reps")
    val selectedMuscle by viewModel.selectedMuscle.observeAsState("abductors")

    val isCreateBtnEnabled = nameValue.trim().isNotEmpty()


    Timber.d("TESTING RECOMPOSITION ${Random.nextInt()}")

    Column {
        BottomSheetStatusBar()
        TopBar(
            statusBarEnabled = false,
            elevationEnabled = false,
            title = "New Exercise",
            strictLeftIconAlignToStart = true,
            leftIconBtn = {
                TopBarIconButton(icon = Icons.Outlined.Close, title = "Back", onClick = {
                    navigator.goBack()
                })
            },
            rightIconBtn = {
                TopBarIconButton(
                    icon = Icons.Outlined.Done,
                    title = "Create",
                    enabled = isCreateBtnEnabled,
                    customTint = MaterialTheme.colors.primary
                ) {
                    viewModel.createExercise()
                    navigator.goBack()
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
                        Row(
                            modifier = Modifier
                                .width((LocalConfiguration.current.screenWidthDp / 2.5).dp)
                                .clickable(onClick = {
                                    viewModel.setCategory(category)
                                }, indication = null,
                                    interactionSource = remember { MutableInteractionSource() })
                        ) {
                            RadioButton(selected = selectedCategory == category, onClick = {
                                viewModel.setCategory(category)
                            })
                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = category.cName
                            )
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
                        Row(
                            modifier = Modifier
                                .width((LocalConfiguration.current.screenWidthDp / 2.5).dp)
                                .clickable(onClick =
                                {
                                    viewModel.setPrimaryMuscle(muscle.tag)
                                }, indication = null,
                                    interactionSource = remember { MutableInteractionSource() })
                        ) {
                            RadioButton(selected = selectedMuscle == muscle.tag, onClick = {
                                viewModel.setPrimaryMuscle(muscle.tag)
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = muscle.name,
                            )
                        }
                    }
                }
            }
        }
    }
}