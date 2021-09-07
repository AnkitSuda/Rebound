package com.ankitsuda.rebound.ui.screens.exercises

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ExercisesScreen(navController: NavHostController) {
    Button(
        onClick = { navController.navigate("exercise_detail") },
        modifier = Modifier.padding(top = 100.dp)
    ) {

    }
}