package com.ankitsuda.rebound.ui.settings.personalization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.ui.components.MoreItemCard
import com.ankitsuda.ui.components.TopBar
import com.ankitsuda.ui.components.TopBarBackIconButton
import com.ankitsuda.ui.components.TopBarIconButton
import com.ankitsuda.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun PersonalizationScreen(
    navController: NavController, navigator: Navigator = LocalNavigator.current,) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Personalization", strictLeftIconAlignToStart = true,leftIconBtn = {
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
                        navigator.navigate(LeafScreen.MainColorsPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = "Shapes",
                    onClick = {
                        navigator.navigate(LeafScreen.ShapesPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = "Cards",
                    onClick = {
                        navigator.navigate(LeafScreen.CardsPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = "Top Bar",
                    onClick = {
                        navigator.navigate(LeafScreen.TopBarPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = "Bottom Bar",
                    onClick = {
                        navigator.navigate(LeafScreen.BottomBarPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = "Charts",
                    onClick = {
                        navigator.navigate(LeafScreen.ChartsPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = "Color Picker Demo",
                    onClick = {
                        navigator.navigate(LeafScreen.ColorPickerDemo().route)
                    })
            }
        }
    }
}