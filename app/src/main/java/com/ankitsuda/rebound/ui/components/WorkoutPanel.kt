package com.ankitsuda.rebound.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.Route
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import timber.log.Timber
import kotlin.random.Random

@Composable
fun WorkoutPanel(
    navController: NavHostController,
    viewModel: WorkoutPanelViewModel = hiltViewModel()
) {
//    var workoutName by remember {
//        mutableStateOf("")
//    }
//    var workoutNote by remember {
//        mutableStateOf("")
//    }

    val currentWorkoutId by viewModel.currentWorkoutId.collectAsState(initial = -1)
    val workout by viewModel.getWorkout(currentWorkoutId).collectAsState(null)

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

        Toast.makeText(
            LocalContext.current,
            "Add ${resultId}",
            Toast.LENGTH_SHORT
        )
            .show()
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

        item {
            Button(
                onClick = {
                    navController.navigate(Route.ExercisesBottomSheet.route)
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

        // Just for testing panel sliding
        items(150) {
            Text(text = it.toString())
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