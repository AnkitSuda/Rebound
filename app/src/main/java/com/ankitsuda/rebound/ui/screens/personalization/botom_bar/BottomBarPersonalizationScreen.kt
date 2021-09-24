package com.ankitsuda.rebound.ui.screens.personalization.botom_bar

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
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.components.settings.RadioGroupCardItem
import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog
import com.ankitsuda.rebound.utils.LabelVisible
import kotlin.math.roundToInt

@Composable
fun BottomBarPersonalizationScreen(
    navController: NavController,
    viewModel: BottomBarPersonalizationViewModel = hiltViewModel()
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val labelVisibleItems = viewModel.labelVisibleItems
    val labelVisible by viewModel.labelVisible.collectAsState(initial = LabelVisible.ALWAYS)

    val labelWeightItems = viewModel.labelWeightItems
    val labelWeight by viewModel.labelWeight.collectAsState(initial = "normal")
    val iconSize by viewModel.iconSize.collectAsState(initial = 24)

        CollapsingToolbarScaffold(
            state = collapsingState,
            toolbar = {
                TopBar(title = "Bottom Bar", strictLeftIconAlignToStart = true,leftIconBtn = {
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
                                viewModel.setLabelVisible(value)
                            },
                            items = labelVisibleItems,
                            selected = labelVisible
                        )

                    }
                    item {

                        RadioGroupCardItem(
                            modifier = itemModifier,
                            text = "Label weight",
                            onSelectionChange = { _, value ->
                                viewModel.setLabelWeight(value)
                            },
                            items = labelWeightItems,
                            selected = labelWeight
                        )
                    }

      item {

                        SliderCardItem(
                            modifier = itemModifier,
                            text = "Icon size",
                            valueRange = 1f..32f,
                            steps = 32,
                            value = iconSize.toFloat(),
                            onChange = {
                                viewModel.setIconSize(it.toInt())
                            }
                        )
                    }

                }
            }
        }

}
