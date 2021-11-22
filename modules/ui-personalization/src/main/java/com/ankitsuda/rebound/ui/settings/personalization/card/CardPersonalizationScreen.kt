package com.ankitsuda.rebound.ui.settings.personalization.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.common.compose.LocalDialog
import com.ankitsuda.common.compose.rememberFlowWithLifecycle
import com.ankitsuda.rebound.ui.ThemeViewModel
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.components.settings.ColorPickerCardItem
import com.ankitsuda.rebound.ui.components.settings.SliderCardItem
import com.ankitsuda.rebound.ui.settings.personalization.top_bar.TopBarPersonalizationScreen
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import kotlin.math.roundToInt

@Composable
fun CardPersonalizationScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(initial = null)

    themeState?.let { theme ->
        CardPersonalizationScreen(
            navController = navController,
            themeState = theme,
            setThemeState = themeViewModel::applyThemeState,
        )

    }
}

@Composable
fun CardPersonalizationScreen(
    navController: NavController,
    themeState: ThemeState,
    setThemeState: (ThemeState) -> Unit,
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Cards", strictLeftIconAlignToStart = true,leftIconBtn = {
                TopBarBackIconButton {
                    navController.popBackStack()
                }
            }, rightIconBtn = {
                TopBarIconButton(icon = Icons.Outlined.Restore, title = "Reset to defaults") {

                }
            })
        },
        modifier = Modifier.background(ReboundTheme.colors.background)
    ) {

        val itemModifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)

        with(LocalDialog.current) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize().background(MaterialTheme.colors.background),
                contentPadding = PaddingValues(16.dp)
            ) {

                item {

                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Background color",
                        selectedColor = themeState.cardColor,
                        onNewColorSelected = {
                            setThemeState(themeState.copy(cardColor = it))
                        })

                }
                item {

                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Border color",
                        selectedColor = themeState.cardBorderColor,
                        onNewColorSelected = {
                            setThemeState(themeState.copy(cardBorderColor = it))
                        })

                }

                item {


                    SliderCardItem(
                        modifier = itemModifier,
                        text = "Border width",
                        value = themeState.cardBorderWidth.toFloat(),
                        steps = 25,
                        valueRange = 0f..25f,
                        onChange = {
                            setThemeState(themeState.copy(cardBorderWidth = it.roundToInt()))
                        })
                }

                item {

                    SliderCardItem(
                        modifier = itemModifier,
                        text = "Elevation",
                        value = themeState.cardElevation.toFloat(),
                        steps = 25,
                        valueRange = 0f..25f,
                        onChange = {
                            setThemeState(themeState.copy(cardElevation = it.roundToInt()))
                        })

                }

            }
        }
    }

}
