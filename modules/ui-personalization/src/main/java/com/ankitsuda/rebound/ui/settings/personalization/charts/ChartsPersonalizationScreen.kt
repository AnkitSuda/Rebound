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
//import com.ankitsuda.rebound.ui.components.*
//import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
//import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
//import com.ankitsuda.rebound.ui.components.settings.RadioGroupCardItem
//import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog

@Composable
fun ChartsPersonalizationScreen(
    navController: NavController,
//    viewModel: ChartsPersonalizationScreenViewModel = hiltViewModel()
) {
//    val collapsingState = rememberCollapsingToolbarScaffoldState()
//
//    val shaderEnabled by viewModel.shaderEnabled.collectAsState(initial = true)
//    val lineThickness by viewModel.lineThickness.collectAsState(initial = 0)
//    val pointDiameter by viewModel.pointDiameter.collectAsState(initial = 0)
//    val pointLineThickness by viewModel.pointLineThickness.collectAsState(initial = 0)
//
//    CollapsingToolbarScaffold(
//        state = collapsingState,
//        toolbar = {
//            TopBar(title = "Charts", strictLeftIconAlignToStart = true, leftIconBtn = {
//                TopBarBackIconButton {
//                    navController.popBackStack()
//                }
//            }, rightIconBtn = {
//                TopBarIconButton(icon = Icons.Outlined.Restore, title = "Reset to defaults") {
//
//                }
//            })
//        },
//        modifier = Modifier.background(MaterialTheme.colors.background)
//    ) {
//
//        val itemModifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = 8.dp)
//
//        with(LocalDialog.current) {
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(MaterialTheme.colors.background),
//                contentPadding = PaddingValues(16.dp)
//            ) {
//
//                item {
//
//                    SwitchCardItem(
//                        modifier = itemModifier,
//                        text = "Shader enabled",
//                        checked = shaderEnabled,
//                        onChange = {
//                            viewModel.setShaderEnabled(it)
//                        }
//                    )
//
//                }
//
//                item {
//
//                    SliderCardItem(
//                        modifier = itemModifier,
//                        text = "Line thickness",
//                        value = lineThickness,
//                        steps = 16,
//                        valueRange = 0f..16f,
//                        onChange = {
//                            viewModel.setLineThickness(it)
//                        }
//                    )
//
//                }
//
//                item {
//
//                    SliderCardItem(
//                        modifier = itemModifier,
//                        text = "Point diameter",
//                        value = pointDiameter,
//                        steps = 16,
//                        valueRange = 0f..16f,
//                        onChange = {
//                            viewModel.setPointDiameter(it)
//                        }
//                    )
//
//                }
//
//                item {
//
//                    SliderCardItem(
//                        modifier = itemModifier,
//                        text = "Point line thickness",
//                        value = pointLineThickness,
//                        steps = 16,
//                        valueRange = 0f..16f,
//                        onChange = {
//                            viewModel.setPointLineThickness(it)
//                        }
//                    )
//
//                }
//
//            }
//        }
//    }


}