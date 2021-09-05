package com.ankitsuda.rebound.ui.screens.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.MainScreenScaffold
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import kotlin.random.Random

/**
 * Root screen of the app
 * Currently testing sliding panel, so using colors instead of other content.
 */
@Composable
fun MainScreen() {

    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.History,
        BottomNavigationScreens.Workout,
        BottomNavigationScreens.Exercises,
        BottomNavigationScreens.More
    )
    MainScreenScaffold(
        modifier = Modifier,
        bottomBar = {
            BottomNavigation(
                contentColor = Color.Black,
                backgroundColor = Color.White,
                elevation = 0.dp,
                modifier = Modifier
                    .navigationBarsHeight(additional = 56.dp)
            ) {
                bottomNavigationItems.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, screen.title) },
                        label = { Text(screen.title) },
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Black.copy(0.4f),
                        alwaysShowLabel = false, // This hides the title for the unselected items
                        modifier = Modifier.navigationBarsPadding(),
                        selected = screen == BottomNavigationScreens.Home,
                        onClick = { },
                    )
                }
            }
        },
        panel = {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {
                Text(
                    text = Random.nextLong().toString(),
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
        },
        panelTopCollapsed = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(end = 8.dp, top = 4.dp, bottom = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = Random.nextLong().toString())
            }
        },
        panelTopExpanded = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(end = 8.dp, top = 4.dp, bottom = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = Random.nextLong().toString())
            }
        }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {

        }
    }
}


sealed class BottomNavigationScreens(val title: String, val icon: ImageVector) {
    object Home :
        BottomNavigationScreens("Home", Icons.Outlined.Home)

    object History :
        BottomNavigationScreens("History", Icons.Outlined.AccessTime)

    object Workout :
        BottomNavigationScreens("Workout", Icons.Outlined.Add)

    object Exercises :
        BottomNavigationScreens("Exercises", Icons.Outlined.FitnessCenter)

    object More :
        BottomNavigationScreens("More", Icons.Outlined.Menu)
}
