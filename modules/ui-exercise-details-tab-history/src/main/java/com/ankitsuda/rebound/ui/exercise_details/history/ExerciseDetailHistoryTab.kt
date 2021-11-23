package com.ankitsuda.rebound.ui.exercise_details.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.domain.entities.LogEntriesWithWorkout
import com.ankitsuda.rebound.ui.components.ExerciseHistoryCardItem

@Composable
fun ExerciseDetailHistoryTab(list: List<LogEntriesWithWorkout>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(list.size) {
            val item = list[it]
            val workout = item.workout
            val entries = item.logEntries

            ExerciseHistoryCardItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                onClick = { },
               workout = workout,
                entries = entries
            )
        }

    }
}