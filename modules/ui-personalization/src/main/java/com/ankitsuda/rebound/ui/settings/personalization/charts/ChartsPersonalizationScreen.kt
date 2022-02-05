package com.ankitsuda.rebound.ui.settings.personalization.charts

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.common.compose.LocalDialog
import com.ankitsuda.common.compose.rememberFlowWithLifecycle
import com.ankitsuda.rebound.ui.ThemeViewModel
import com.ankitsuda.rebound.ui.settings.personalization.card.CardPersonalizationScreen


import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.components.settings.ColorPickerCardItem
import com.ankitsuda.rebound.ui.components.settings.SliderCardItem
import com.ankitsuda.rebound.ui.components.settings.SwitchCardItem
import me.onebone.toolbar.ScrollStrategy

@Composable
fun ChartsPersonalizationScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(initial = null)

    themeState?.let { theme ->
        ChartsPersonalizationScreen(
            navController = navController,
            themeState = theme,
            setThemeState = themeViewModel::applyThemeState,
        )

    }
}

@Composable
fun ChartsPersonalizationScreen(
    navController: NavController,
    themeState: ThemeState,
    setThemeState: (ThemeState) -> Unit,
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar(title = "Charts", strictLeftIconAlignToStart = true, leftIconBtn = {
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

        val itemModifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)

        with(LocalDialog.current) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentPadding = PaddingValues(16.dp)
            ) {

                item {

                    SwitchCardItem(
                        modifier = itemModifier,
                        text = "Shader enabled",
                        checked = themeState.chartsShaderEnabled,
                        onChange = {
                            setThemeState(themeState.copy(chartsShaderEnabled = it))
                        }
                    )

                }

                item {

                    SliderCardItem(
                        modifier = itemModifier,
                        text = "Line thickness",
                        value = themeState.chartsLineThickness,
                        steps = 16,
                        valueRange = 0f..16f,
                        onChange = {
                            setThemeState(themeState.copy(chartsLineThickness = it))
                        }
                    )

                }

                item {

                    SliderCardItem(
                        modifier = itemModifier,
                        text = "Point diameter",
                        value = themeState.chartsPointDiameter,
                        steps = 16,
                        valueRange = 0f..16f,
                        onChange = {
                            setThemeState(themeState.copy(chartsPointDiameter = it))
                        }
                    )

                }

                item {

                    SliderCardItem(
                        modifier = itemModifier,
                        text = "Point line thickness",
                        value = themeState.chartsPointLineThickness,
                        steps = 16,
                        valueRange = 0f..16f,
                        onChange = {
                            setThemeState(themeState.copy(chartsPointLineThickness = it))
                        }
                    )

                }

            }
        }
    }


}