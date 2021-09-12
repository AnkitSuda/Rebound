package com.ankitsuda.rebound.ui.screens.more

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.BuildConfig
import com.ankitsuda.rebound.ui.Route
import com.ankitsuda.rebound.ui.components.MoreItemCard
import com.ankitsuda.rebound.ui.components.RoutineItemCard
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun MoreScreen(navController: NavHostController) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "More")
        },
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.BubbleChart,
                    text = "Personalization",
                    description = "Make rebound yours",
                    onClick = {
                        navController.navigate(Route.Personalization.route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.Palette,
                    text = "App Theme",
                    description = "Light",
                    onClick = {

                    })
            }
            item {
                SectionHeader(text = "Defaults")
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.FitnessCenter,
                    text = "Weight Unit",
                    description = "Metric (kg)",
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.DirectionsRun,
                    text = "Distance Unit",
                    description = "Metric (m/km)",
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.Event,
                    text = "First Day of The Week",
                    description = "Sunday",
                    onClick = {

                    })
            }
            item {
                SectionHeader(text = "Your Data")
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.Folder,
                    text = "Backup Data",
                    description = "To JSON",
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.Restore,
                    text = "Restore Data",
                    description = "From a previous backup",
                    onClick = {

                    })
            }

            item {
                SectionHeader(text = "Feedback")
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.ThumbsUpDown,
                    text = "Write a Review",
                    description = "It will motivate us to make rebound more better.",
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.BugReport,
                    text = "Suggestions & Bug Report",
                    description = "You can open an issue in Github repository",
                    onClick = {

                    })
            }
            item {
                SectionHeader(text = "About")
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.Info,
                    text = "About app",
                    onClick = {

                    })
            }
            item {
                Text(
                    text = "v${BuildConfig.VERSION_NAME}",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}

@Composable
fun SectionHeader(text: String) {
    Text(text = text, style = MaterialTheme.typography.caption, modifier = Modifier.padding(8.dp))
}

