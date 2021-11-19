package com.ankitsuda.rebound.ui.settings.personalization.shapes

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
//import com.ankitsuda.rebound.ui.components.*
//import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
//import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
//import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog

@Composable
fun ShapesPersonalizationScreen(
    navController: NavController,
    viewModel: ShapesPersonalizationScreenViewModel = hiltViewModel()
) {
//    val collapsingState = rememberCollapsingToolbarScaffoldState()
//
//    val smallTopStart by viewModel.smallTopStart.collectAsState(0)
//    val smallTopEnd by viewModel.smallTopEnd.collectAsState(0)
//    val smallBottomStart by viewModel.smallBottomStart.collectAsState(0)
//    val smallBottomEnd by viewModel.smallBottomEnd.collectAsState(0)
//
//    CollapsingToolbarScaffold(
//        state = collapsingState,
//        toolbar = {
//            TopBar(title = "Shapes", strictLeftIconAlignToStart = true,leftIconBtn = {
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
//
//                item {
//
//                    ShapesEditorCardItem(
//                        modifier = itemModifier,
//                        text = "Small shape",
//                        defaultValues = ShapeValues(
//                            topStart = smallTopStart,
//                            topEnd = smallTopEnd,
//                            bottomStart = smallBottomStart,
//                            bottomEnd = smallBottomEnd,
//                        ),
//                        onValueChange = {
//                            viewModel.setSmalShapeValues(it)
//                        }
//                    )
//
//                }
//
//
//                item {
//
//                    ShapesEditorCardItem(
//                        modifier = itemModifier,
//                        text = "Small shape",
//                        defaultValues = ShapeValues(
//                            topStart = smallTopStart,
//                            topEnd = smallTopEnd,
//                            bottomStart = smallBottomStart,
//                            bottomEnd = smallBottomEnd,
//                        ),
//                        onValueChange = {
//                            viewModel.setSmalShapeValues(it)
//                        }
//                    )
//
//                }
//
//
//            }
//        }
//    }
}
