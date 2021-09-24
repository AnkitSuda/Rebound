package com.ankitsuda.rebound.ui.screens.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.components.TopSearchBar
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.screens.create_exercise.CreateExerciseScreen
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ExerciseDetailScreen(navController: NavHostController) {
    val tabData = listOf(
        "Charts",
        "History",
        "About"
    )
    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0,
    )
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    CollapsingToolbarScaffold(
        toolbar = {
            Surface(
                Modifier
                    .background(ReboundTheme.colors.background)
                    .zIndex(2f), elevation = 2.dp
            ) {
                Column() {
                    TopBar(
                        elevationEnabled = false,
                        title = "Exercise",
                        strictLeftIconAlignToStart = true,
                        leftIconBtn = {
                            TopBarBackIconButton(onClick = {
                                navController.popBackStack()
                            })
                        },
                        rightIconBtn = {
                            TopBarIconButton(
                                icon = Icons.Outlined.StarBorder,
                                title = "Favorite",
                                onClick = {

                                })
                        })

                    TabRow(
                        selectedTabIndex = tabIndex,
                        backgroundColor = ReboundTheme.colors.background,
                        divider = { Divider(thickness = 0.dp) },
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                            )
                        }
                    ) {
                        tabData.forEachIndexed { index, pair ->
                            Tab(selected = tabIndex == index, onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }, text = {
                                Text(text = pair)

                            }
                            )
                        }
                    }
                }
            }
        },
    ) {


        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { index ->
            when (index) {
                0 -> {
                    ExerciseDetailChartsTab()
                }
                1 -> {
                    ExerciseDetailHistoryTab()
                }
                2 -> {
                    ExerciseDetailAboutTab()
                }
            }

        }
    }
}