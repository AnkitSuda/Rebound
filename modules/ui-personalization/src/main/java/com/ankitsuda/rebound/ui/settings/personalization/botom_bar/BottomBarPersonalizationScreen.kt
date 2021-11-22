package com.ankitsuda.rebound.ui.settings.personalization.botom_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.ankitsuda.base.util.LabelVisible
import com.ankitsuda.common.compose.LocalDialog
import com.ankitsuda.common.compose.rememberFlowWithLifecycle
import com.ankitsuda.rebound.ui.ThemeViewModel
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.components.settings.RadioGroupCardItem
import com.ankitsuda.rebound.ui.components.settings.SliderCardItem
import com.ankitsuda.rebound.ui.settings.personalization.top_bar.TopBarPersonalizationScreen
import kotlin.math.roundToInt

@Composable
fun BottomBarPersonalizationScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(initial = null)

    themeState?.let { theme ->
        BottomBarPersonalizationScreen(
            navController = navController,
            themeState = theme,
            setThemeState = themeViewModel::applyThemeState,
        )

    }
}

@Composable
fun BottomBarPersonalizationScreen(
    navController: NavController,
    themeState: ThemeState,
    setThemeState: (ThemeState) -> Unit,
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val labelVisibleItems = LabelVisible.values()
    val labelWeightItems = listOf(
        "normal",
        "bold"
    )
    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Bottom Bar", strictLeftIconAlignToStart = true, leftIconBtn = {
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

                    RadioGroupCardItem(
                        modifier = itemModifier,
                        text = "Label visible",
                        onSelectionChange = { _, value ->
                            setThemeState(themeState.copy(bottomBarLabelVisible = value))
                        },
                        items = labelVisibleItems,
                        selected = themeState.bottomBarLabelVisible
                    )

                }
                item {

                    RadioGroupCardItem(
                        modifier = itemModifier,
                        text = "Label weight",
                        onSelectionChange = { _, value ->
                            setThemeState(themeState.copy(bottomBarLabelWeight = value))
                        },
                        items = labelWeightItems,
                        selected = themeState.bottomBarLabelWeight
                    )
                }

                item {

                    SliderCardItem(
                        modifier = itemModifier,
                        text = "Icon size",
                        valueRange = 1f..32f,
                        steps = 32,
                        value = themeState.bottomBarIconSize.toFloat(),
                        onChange = {
                            setThemeState(themeState.copy(bottomBarIconSize = it.roundToInt()))
                        }
                    )
                }

            }
        }
    }

}
