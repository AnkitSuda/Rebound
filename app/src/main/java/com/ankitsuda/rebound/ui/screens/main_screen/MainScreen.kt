package com.ankitsuda.rebound.ui.screens.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ankitsuda.rebound.ui.MainScreenScaffold
import com.ankitsuda.rebound.ui.components.PanelTopCollapsed
import com.ankitsuda.rebound.ui.components.PanelTopDragHandle
import com.ankitsuda.rebound.ui.components.PanelTopExpanded
import com.ankitsuda.rebound.ui.screens.exercises.ExercisesScreen
import com.ankitsuda.rebound.ui.screens.history.HistoryScreen
import com.ankitsuda.rebound.ui.screens.home.HomeScreen
import com.ankitsuda.rebound.ui.screens.more.MoreScreen
import com.ankitsuda.rebound.ui.screens.workout.WorkoutScreen
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Root screen of the app
 * Currently testing sliding panel, so using colors instead of other content.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    var panelTopHeight by remember {
        mutableStateOf(0)
    }

    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.History,
        BottomNavigationScreens.Workout,
        BottomNavigationScreens.Exercises,
        BottomNavigationScreens.More
    )

    val swipeableState = rememberSwipeableState(0)
    val coroutine = rememberCoroutineScope()

    MainScreenScaffold(
        modifier = Modifier,
        swipeableState = swipeableState,
        onPanelTopHeightChange = {
            panelTopHeight = it
        },
        bottomBar = {
            BottomNavigation(
                contentColor = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp,
                modifier = Modifier
                    .navigationBarsHeight(additional = 56.dp)
            ) {
                val currentRoute = currentRoute(navController)

                bottomNavigationItems.forEach { screen ->

                    BottomNavigationItem(
                        icon = { Icon(screen.icon, screen.title) },
                        label = { Text(screen.title) },
                        selectedContentColor = MaterialTheme.colors.primary,
                        unselectedContentColor = Color.Black.copy(0.4f),
                        alwaysShowLabel = false, // This hides the title for the unselected items
                        modifier = Modifier.navigationBarsPadding(),
                        selected = currentRoute == screen.route,
                        onClick = {
                            // This if check gives us a "singleTop" behavior where we do not create a
                            // second instance of the composable if we are already on that destination
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route)
                            }
                        },
                    )
                }
            }
        },
        panel = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Text(
                    text = Random.nextLong().toString(),
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
        },
        panelTopCommon = {
            PanelTopDragHandle()
        },
        panelTopCollapsed = {
            PanelTopCollapsed()
        },
        panelTopExpanded = {
            PanelTopExpanded(
                onCollapseBtnClicked = {
                    coroutine.launch {
                        swipeableState.animateTo(0)
                    }
                },
                onTimerBtnClicked = { },
                onFinishBtnClicked = {})
        }) {
        MainScreenNavigationConfigurations(navController = navController, panelTopHeight)
    }
}

@Composable
fun MainScreenNavigationConfigurations(navController: NavHostController, panelTopHeight: Int) {
    val panelTopHeightDp = with(LocalDensity.current) { panelTopHeight.toDp() }

    NavHost(navController, startDestination = BottomNavigationScreens.Home.route) {
        // Bottom Nav
        composable(BottomNavigationScreens.Home.route) {
            HomeScreen(navController)
        }
        composable(BottomNavigationScreens.History.route) {
            HistoryScreen(navController, panelTopHeightDp)
        }
        composable(BottomNavigationScreens.Workout.route) {
            WorkoutScreen(navController, panelTopHeightDp)
        }
        composable(BottomNavigationScreens.Exercises.route) {
            ExercisesScreen(navController)
        }
        composable(BottomNavigationScreens.More.route) {
            MoreScreen(navController)
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

sealed class BottomNavigationScreens(val route: String, val title: String, val icon: ImageVector) {
    object Home :
        BottomNavigationScreens("home", "Home", Icons.Outlined.Home)

    object History :
        BottomNavigationScreens("history", "History", Icons.Outlined.AccessTime)

    object Workout :
        BottomNavigationScreens("workout", "Workout", Icons.Outlined.Add)

    object Exercises :
        BottomNavigationScreens("exercises", "Exercises", Icons.Outlined.FitnessCenter)

    object More :
        BottomNavigationScreens("more", "More", Icons.Outlined.Menu)
}
