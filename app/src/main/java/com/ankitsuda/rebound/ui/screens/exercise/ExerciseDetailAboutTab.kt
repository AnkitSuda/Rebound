package com.ankitsuda.rebound.ui.screens.exercise

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.data.entities.Exercise
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun ExerciseDetailAboutTab(exercise: Exercise) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (!exercise.notes.isNullOrBlank()) {
            Section(title = "Instructions", text = exercise.notes.toString())
        }

        if (!exercise.primaryMuscleTag.isNullOrBlank()) {
            Section(title = "Primary Muscle", text = exercise.primaryMuscleTag.toString())

        }

        Section(title = "Category", text = exercise.category.cName)
    }
}

@Composable
private fun ColumnScope.Section(title: String, text: String) {
    Text(text = title, style = ReboundTheme.typography.caption)
    RSpacer(space = 4.dp)
    Text(text = text, style = ReboundTheme.typography.body1)
    RSpacer(space = 16.dp)
}