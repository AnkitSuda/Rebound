package com.ankitsuda.rebound.ui.settings.personalization.card

import androidx.compose.runtime.*
import androidx.navigation.NavController

@Composable
fun CardPersonalizationScreen(
    navController: NavController,
//    viewModel: CardPersonalizationScreenViewModel = hiltViewModel()
) {
//    val collapsingState = rememberCollapsingToolbarScaffoldState()
//
//    val cardColor by viewModel.cardColor.collectAsState(Color(248, 248, 248))
//    val borderColor by viewModel.borderColor.collectAsState(Color(248, 248, 248))
//    val borderWidth by viewModel.borderWidth.collectAsState(0)
//    val elevation by viewModel.elevation.collectAsState(0)
//
//    Timber.d("cardColor $cardColor")
//
//    CollapsingToolbarScaffold(
//        state = collapsingState,
//        toolbar = {
//            TopBar(title = "Cards", strictLeftIconAlignToStart = true,leftIconBtn = {
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
//                    .fillMaxSize().background(MaterialTheme.colors.background),
//                contentPadding = PaddingValues(16.dp)
//            ) {
//
//                item {
//
//                    ColorPickerCardItem(
//                        modifier = itemModifier,
//                        text = "Background color",
//                        selectedColor = cardColor,
//                        onNewColorSelected = {
//                            viewModel.setCardColor(it)
//                        })
//
//                }
//                item {
//
//                    ColorPickerCardItem(
//                        modifier = itemModifier,
//                        text = "Border color",
//                        selectedColor = borderColor,
//                        onNewColorSelected = {
//                            viewModel.setBorderColor(it)
//                        })
//
//                }
//
//                item {
//
//
//                    SliderCardItem(
//                        modifier = itemModifier,
//                        text = "Border width",
//                        value = borderWidth.toFloat(),
//                        steps = 25,
//                        valueRange = 0f..25f,
//                        onChange = {
//                            viewModel.setBorderWidth(it.toInt())
//                        })
//                }
//
//                item {
//
//                    SliderCardItem(
//                        modifier = itemModifier,
//                        text = "Elevation",
//                        value = elevation.toFloat(),
//                        steps = 25,
//                        valueRange = 0f..25f,
//                        onChange = {
//                            viewModel.setElevation(it.toInt())
//                        })
//
//                }
//
//            }
//        }
//    }

}
