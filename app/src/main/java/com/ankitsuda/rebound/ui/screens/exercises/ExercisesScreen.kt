package com.ankitsuda.rebound.ui.screens.exercises

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.ExerciseItem
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.components.TopSearchBar
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(
    ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun ExercisesScreen(
    navController: NavHostController,
    viewModel: ExercisesScreenViewModel = hiltViewModel()
) {
    val tabData = arrayListOf<String>()

    repeat(10) { tabData.add("Muscle") }

    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0,
    )
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()


    val isSearchMode by viewModel.isSearchMode.observeAsState(false)
    val searchTerm by viewModel.searchTerm.observeAsState("")

    Column {
        Box() {

            if (!isSearchMode) {
                TopBar(title = "Exercises", leftIconBtn = {
                    TopBarIconButton(icon = Icons.Outlined.Search, title = "Search", onClick = {
                        viewModel.toggleSearchMode()
                    })
                }, rightIconBtn = {
                    TopBarIconButton(
                        icon = Icons.Outlined.Add,
                        title = "Create Exercise",
                        onClick = {

                        })
                })

            } else {
                TopSearchBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = "Search here...",
                    value = searchTerm,
                    onBackClick = {
                        viewModel.toggleSearchMode()
                    },
                    onValueChange = {
                        viewModel.setSearchTerm(it)
                    },
                )
            }
        }



        ScrollableTabRow(
            selectedTabIndex = tabIndex,
            backgroundColor = MaterialTheme.colors.background,
            edgePadding = 0.dp,
            divider = { Divider(thickness = 0.dp) },
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            tabData.forEachIndexed { index, pair ->
                Tab(
                    selected = tabIndex == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = pair)

                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { index ->

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(25) {
                    ExerciseItem(
                        name = "Bench Press: Barbell",
                        muscle = "Chest",
                        totalLogs = it,
                        onClick = {
                            navController.navigate("exercise_detail")
                        })
                }
            }
        }

    }
}