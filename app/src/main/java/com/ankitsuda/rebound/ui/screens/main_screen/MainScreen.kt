package com.ankitsuda.rebound.ui.screens.main_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.ankitsuda.rebound.ui.navigation.MainScreenNavigationConfigurations
import com.ankitsuda.rebound.ui.MainScreenScaffold
import com.ankitsuda.rebound.ui.navigation.Route
import com.ankitsuda.rebound.ui.components.panel_tops.PanelTopCollapsed
import com.ankitsuda.rebound.ui.components.panel_tops.PanelTopDragHandle
import com.ankitsuda.rebound.ui.components.panel_tops.PanelTopExpanded
import com.ankitsuda.rebound.ui.components.workout_panel.WorkoutPanel
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.utils.LabelVisible
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.navigation.material.BottomSheetNavigatorSheetState
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import kotlinx.coroutines.launch
import timber.log.Timber

data class MainDialog(
    var dialogContent: @Composable () -> Unit = {},
    var showDialog: () -> Unit = {},
    var hideDialog: () -> Unit = {}
)

data class MainBottomSheet  @OptIn(ExperimentalMaterialNavigationApi::class) constructor(
    var state: BottomSheetNavigatorSheetState? = null,
    var sheetContent: @Composable () -> Unit = {},
    var showSheet: () -> Unit = {},
    var hideSheet: () -> Unit = {}
) {
    fun hide() {
        hideSheet()
    }

    fun show(content: @Composable () -> Unit) {
        sheetContent = content
        showSheet()
    }
}

val LocalDialog = compositionLocalOf { MainDialog() }
val LocalBottomSheet = compositionLocalOf { MainBottomSheet() }

/**
 * Root screen of the app
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    navController.navigatorProvider += bottomSheetNavigator


    val swipeableState = rememberSwipeableState(0)
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutine = rememberCoroutineScope()


    val currentWorkoutId by viewModel.currentWorkoutId.collectAsState(initial = -1)

    val panelHidden = currentWorkoutId == (-1).toLong()


    Timber.d("currentWorkoutId $currentWorkoutId panelHidden $panelHidden")

    BackHandler(swipeableState.currentValue != 0) {
        coroutine.launch {
            swipeableState.animateTo(0)
        }
    }

    var dialogContent: @Composable () -> Unit by remember {
        mutableStateOf({})
    }

    var sheetContent: @Composable () -> Unit by remember {
        mutableStateOf({})
    }

    var dialogVisible by remember {
        mutableStateOf(false)
    }

    // Dialog
    val dialog = MainDialog()
    dialog.showDialog = {
        dialogContent = dialog.dialogContent
        dialogVisible = true
        Timber.d("show dialog")
    }
    dialog.hideDialog = {
        dialogVisible = false
    }

    // Bottom sheet
    val bottomSheet = MainBottomSheet()
    bottomSheet.showSheet = {
        sheetContent = bottomSheet.sheetContent
        coroutine.launch {
            sheetState.show()
        }
        Timber.d("show sheet")
    }
    bottomSheet.hideSheet = {
        coroutine.launch {
            sheetState.hide()
        }
    }

    if (bottomSheet.state == null) {
        bottomSheet.state = bottomSheetNavigator.navigatorSheetState
    }

    CompositionLocalProvider(LocalDialog provides dialog, LocalBottomSheet provides bottomSheet) {
        Box() {
            /**
             * Temporary using ModalBottomSheetLayout
             * will create a custom implementation later in MainScreenScaffold with proper status bar padding
             * and auto corner radius
             */
            com.google.accompanist.navigation.material.ModalBottomSheetLayout(
//                sheetState = sheetState,
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
//                sheetContent = {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .defaultMinSize(minHeight = 100.dp)
//                    ) {
//
//                        sheetContent()
//
//                    }
//                },
                bottomSheetNavigator = bottomSheetNavigator
            ) {
                MainScreenScaffold(
                    modifier = Modifier,
                    panelHidden = panelHidden,
                    swipeableState = swipeableState,
                    bottomBar = {
                        BottomBar(
                            elevationEnabled = panelHidden,
                            navController = navController,
                            viewModel
                        )
                    },
                    panel = {
                        WorkoutPanel(navController)
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
                    MainScreenNavigationConfigurations(navController = navController)
                }
            }

            if (dialogVisible) {
                AlertDialog(onDismissRequest = {
                    dialogVisible = false
                },
                    buttons = {
                        dialogContent()
                    })
            }
        }
    }
}

@Composable
private fun BottomBar(
    elevationEnabled: Boolean = false,
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel
) {
    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.History,
        BottomNavigationScreens.Workout,
        BottomNavigationScreens.Exercises,
        BottomNavigationScreens.More
    )

    val labelVisible by mainScreenViewModel.bottomBarLabelVisible.collectAsState(initial = LabelVisible.ALWAYS)
    val labelWeight by mainScreenViewModel.labelWeight.collectAsState(initial = "normal")
    val iconSize by mainScreenViewModel.iconSize.collectAsState(initial = 24)



    BottomNavigation(
        contentColor = MaterialTheme.colors.primary,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = if (elevationEnabled) 8.dp else 0.dp,
        modifier = Modifier
            .navigationBarsHeight(additional = 56.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavigationItems.forEach { screen ->

            BottomNavigationItem(
                icon = { Icon(screen.icon, screen.title, Modifier.size(iconSize.dp)) },
                label = if (labelVisible == LabelVisible.NEVER) {
                    null
                } else {
                    {

                        Text(
                            screen.title, fontWeight = when (labelWeight) {
                                "bold" -> FontWeight.Bold
                                else -> FontWeight.Normal
                            }
                        )

                    }
                },
                selectedContentColor = ReboundTheme.colors.primary,
                unselectedContentColor = ReboundTheme.colors.onBackground.copy(0.4f),
                alwaysShowLabel = labelVisible == LabelVisible.ALWAYS,
                modifier = Modifier.navigationBarsPadding(),
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
//                               if (currentRoute != screen.route) {
//                                   navController.navigate(screen.route)
//                               }
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }

                },
            )
        }
    }
}

sealed class BottomNavigationScreens(val route: String, val title: String, val icon: ImageVector) {
    object Home :
        BottomNavigationScreens(Route.HomeTab.route, "Home", Icons.Outlined.Home)

    object History :
        BottomNavigationScreens(Route.HistoryTab.route, "History", Icons.Outlined.AccessTime)

    object Workout :
        BottomNavigationScreens(Route.WorkoutTab.route, "Workout", Icons.Outlined.PlayArrow)

    object Exercises :
        BottomNavigationScreens(Route.ExercisesTab.route, "Exercises", Icons.Outlined.FitnessCenter)

    object More :
        BottomNavigationScreens(Route.MoreTab.route, "More", Icons.Outlined.Menu)
}
