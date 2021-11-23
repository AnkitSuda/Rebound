package com.ankitsuda.rebound.ui.components.workout_panel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.AppTextField
import com.ankitsuda.rebound.ui.components.WorkoutExerciseItemAlt
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import timber.log.Timber

@Composable
fun WorkoutPanel(
    navController: NavHostController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: WorkoutPanelViewModel = hiltViewModel()
) {
    val currentWorkoutId by viewModel.currentWorkoutId.collectAsState(initial = -1)
    val workout by viewModel.getWorkout(currentWorkoutId).collectAsState(null)
    val logEntriesWithJunction by viewModel.getLogEntriesWithExerciseJunction()
        .collectAsState(emptyList())

    Timber.d("logEntriesWithJunction $logEntriesWithJunction")

    if (viewModel.mWorkout == null || viewModel.mWorkout != workout) {
        Timber.d("Updating viewModel mWorkout to currentWorkout")
        viewModel.mWorkout = workout
    }

    val workoutName = workout?.name ?: ""
    val workoutNote = workout?.note ?: ""

    // Observes results when ExercisesScreen changes value of arg
    val exercisesScreenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Long?>("result_exercises_screen_exercise_id")?.observeAsState()
    exercisesScreenResult?.value?.let { resultId ->

        viewModel.addExerciseToWorkout(resultId)

        navController.currentBackStackEntry?.savedStateHandle?.set(
            "result_exercises_screen_exercise_id",
            null
        )

    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ReboundTheme.colors.background)
    ) {
        item {
            Column() {
                WorkoutQuickInfo()
                Divider()
            }
        }
        item {
            Text(text = "TEST: current workout id $currentWorkoutId")
        }
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                AppTextField(
                    value = workoutName,
                    onValueChange = { viewModel.updateWorkoutName(it) },
                    placeholderValue = "Workout name",
                    modifier = Modifier.fillMaxWidth()
                )
                AppTextField(
                    value = workoutNote,
                    onValueChange = { viewModel.updateWorkoutNote(it) },
                    placeholderValue = "Workout note",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }


        // Just for testing panel sliding
//        items(logEntriesWithJunction.size) {
//            val item = logEntriesWithJunction[it]
//            val junction = item.junction
//            val logEntries = item.logEntries
//
//            WorkoutExerciseItem(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp),
//                exerciseLogEntries = logEntries,
//                onWeightChange = { entry, value ->
//                    viewModel.updateLogEntry(entry.copy(weight = value))
//                },
//                onRepsChange = { entry, value ->
//                    viewModel.updateLogEntry(entry.copy(reps = value))
//                },
//                onCompleteChange = { entry, value ->
//                    viewModel.updateLogEntry(entry.copy(completed = value))
//                },
//                onSwipeDelete = { entryToDelete ->
//                    Timber.d("Swiped entry $entryToDelete")
//                    viewModel.deleteLogEntry(entryToDelete)
//                },
//                onAddSet = {
//                    viewModel.addEmptySetToExercise(
//                        try {
//                            logEntries[logEntries.size - 1].setNumber + 1
//                        } catch (e: Exception) {
//                            1
//                        },
//                        junction
//                    )
//                }
//            )
//        }

        for (logEntriesWithJunctionItem in logEntriesWithJunction) {
            WorkoutExerciseItemAlt(
                logEntriesWithJunction = logEntriesWithJunctionItem,
                onValuesUpdated = { updatedEntry ->
                    viewModel.updateLogEntry(updatedEntry)
                },
                onSwipeDelete = { entryToDelete ->
                    Timber.d("Swiped entry $entryToDelete")
                    viewModel.deleteLogEntry(entryToDelete)
                },
                onAddSet = {
                    viewModel.addEmptySetToExercise(
                        try {
                            logEntriesWithJunctionItem.logEntries[logEntriesWithJunctionItem.logEntries.size - 1].setNumber!! + 1
                        } catch (e: Exception) {
                            1
                        },
                        logEntriesWithJunctionItem.junction
                    )
                },
                onDeleteExercise = {
                    viewModel.deleteExerciseFromWorkout(logEntriesWithJunctionItem)
                }
            )
        }

        item {
            Button(
                onClick = {

                          navigator.navigate(LeafScreen.ExercisesBottomSheet().route)
//                    navController.navigate(Route.ExercisesBottomSheet.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp),
                    tint = MaterialTheme.colors.onPrimary
                )
                Text(text = "Add Exercise", style = MaterialTheme.typography.button)
            }
        }

        item {
            TextButton(
                onClick = {
                    viewModel.cancelCurrentWorkout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp),
                    tint = Color.Red
                )
                Text(
                    text = "Cancel Workout",
                    style = MaterialTheme.typography.button,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
private fun WorkoutQuickInfo() {

    FlowRow(
        mainAxisAlignment = MainAxisAlignment.SpaceEvenly,
        mainAxisSize = SizeMode.Expand,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
    ) {
        repeat(3) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "15 min",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = "Duration",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                    color = Color(117, 117, 117),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}