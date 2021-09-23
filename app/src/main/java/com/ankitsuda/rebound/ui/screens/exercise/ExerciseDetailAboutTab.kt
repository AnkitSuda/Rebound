package com.ankitsuda.rebound.ui.screens.exercise

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun ExerciseDetailAboutTab() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = "Instructions", style = ReboundTheme.typography.overline)
        RSpacer(space = 4.dp)
        Text(text = "Notes here", style = ReboundTheme.typography.body2)

        RSpacer(space = 16.dp)

        Text(text = "Primary Muscle", style = ReboundTheme.typography.overline)
        RSpacer(space = 4.dp)
        Text(text = "Chest", style = ReboundTheme.typography.body2)

        RSpacer(space = 16.dp)

        Text(text = "Category", style = ReboundTheme.typography.overline)
        RSpacer(space = 4.dp)
        Text(text = "Weight & Reps", style = ReboundTheme.typography.body2)

    }

}