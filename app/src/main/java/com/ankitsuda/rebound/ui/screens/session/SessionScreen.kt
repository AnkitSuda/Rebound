package com.ankitsuda.rebound.ui.screens.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import kotlin.random.Random

@Composable
fun SessionScreen(navController: NavHostController) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val sessionId by remember {
        mutableStateOf(
            navController.currentBackStackEntry?.arguments?.getString("sessionId")?.toLong()
        )
    }

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Session $sessionId", leftIconBtn = {
                TopBarBackIconButton {
                    navController.popBackStack()
                }
            }, rightIconBtn = {
                TopBarIconButton(icon = Icons.Outlined.MoreVert, title = "Open Menu") {

                }
            })
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column() {
                        Text(text = "7 Exercises")
                        SessionCompleteQuickInfo(
                            time = "45 m",
                            volume = "1000 kg",
                            prs = 2
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit session")
                    }
                }
            }

            items(10) {
                SessionExerciseCardItem(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    onClick = { },
                    exerciseName = "Bench Press: Barbell",
                    sets = listOf(
                        Pair(Random.nextInt(10, 50), Random.nextInt(1, 8)),
                        Pair(Random.nextInt(50, 75), Random.nextInt(8, 12)),
                        Pair(Random.nextInt(75, 100), Random.nextInt(12, 20)),
                    )
                )
            }
        }

    }
}