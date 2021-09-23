package com.ankitsuda.rebound.ui.screens.exercise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.ExerciseHistoryCardItem
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import kotlin.random.Random

@Composable
fun ExerciseDetailHistoryTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(10) {
            ExerciseHistoryCardItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                onClick = { },
                date = "Monday, 20 September, 2021, 7:45 AM",
                sets = listOf(
                    Pair(Random.nextInt(10, 50), Random.nextInt(1, 8)),
                    Pair(Random.nextInt(50, 75), Random.nextInt(8, 12)),
                    Pair(Random.nextInt(75, 100), Random.nextInt(12, 20)),
                )
            )
        }

    }
}