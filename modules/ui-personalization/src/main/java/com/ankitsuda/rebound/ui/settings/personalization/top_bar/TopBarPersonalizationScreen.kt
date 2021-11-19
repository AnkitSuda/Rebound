package com.ankitsuda.rebound.ui.settings.personalization.top_bar

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun TopBarPersonalizationScreen(
    navController: NavController,
//    viewModel: TopBarPersonalizationScreenViewModel = hiltViewModel()
) {
//    val collapsingState = rememberCollapsingToolbarScaffoldState()
//
//    val titleAlignment by viewModel.titleAlignment.collectAsState("center")
//    val backgroundColor by viewModel.backgroundColor.collectAsState(Color.White)
//    val contentColor by viewModel.contentColor.collectAsState(Color.White)
//    val elevation by viewModel.elevation.collectAsState(0)
//
//    CollapsingToolbarScaffold(
//        state = collapsingState,
//        toolbar = {
//            TopBar(title = "Top Bar", strictLeftIconAlignToStart = true,leftIconBtn = {
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
//                    RadioGroupCardItem(
//                        modifier = itemModifier,
//                        text = "Title alignment",
//                        onSelectionChange = { _, value ->
//                            viewModel.setTitleAlignment(value)
//                        },
//                        items = viewModel.allTitleAlignments,
//                        selected = titleAlignment
//                    )
//                }
//
//                item {
//                    ColorPickerCardItem(
//                        modifier = itemModifier,
//                        text = "Background color",
//                        selectedColor = backgroundColor,
//                        onNewColorSelected = {
//                            viewModel.setBackgroundColor(it)
//                        })
//
//                }
//
//                item {
//                    ColorPickerCardItem(
//                        modifier = itemModifier,
//                        text = "Content color",
//                        selectedColor = contentColor,
//                        onNewColorSelected = {
//                            viewModel.setContentColor(it)
//                        })
//
//                }
//
//                item {
//
//                    SliderCardItem(
//                        modifier = itemModifier,
//                        text = "Elevation",
//                        value = elevation,
//                        steps = 50,
//                        valueRange = 0f..50f,
//                        onChange = {
//                            viewModel.setElevation(it)
//                        }
//                    )
//
//                }
//
//            }
//        }
//    }

}
