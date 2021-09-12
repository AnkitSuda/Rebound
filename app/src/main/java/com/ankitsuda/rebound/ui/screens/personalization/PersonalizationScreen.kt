package com.ankitsuda.rebound.ui.screens.personalization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BubbleChart
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ankitsuda.rebound.ui.Route
import com.ankitsuda.rebound.ui.components.MoreItemCard
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.screens.more.SectionHeader

@Composable
fun PersonalizationScreen(navController: NavController) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Personalization", leftIconBtn = {
                TopBarBackIconButton {
                    navController.popBackStack()
                }
            }, rightIconBtn = {
                TopBarIconButton(icon = Icons.Outlined.Restore, title = "Reset to defaults") {

                }
            })
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth().background(MaterialTheme.colors.background)
                        .padding(bottom = 8.dp),
                    text = "Main Colors",
                    onClick = {
                        navController.navigate(Route.MainColorsPersonalization.route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = "Cards",
                    onClick = {
                        navController.navigate(Route.CardsPersonalization.route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = "Color Picker Demo",
                    onClick = {
                        navController.navigate(Route.ColorPickerDemo.route)
                    })
            }
        }
    }
}