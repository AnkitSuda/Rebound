package com.ankitsuda.rebound.ui.screens.personalization.card

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.components.color_picker.ColorPicker
import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog
import timber.log.Timber

@Composable
fun CardPersonalizationScreen(
    navController: NavHostController,
    viewModel: CardPersonalizationScreenViewModel = hiltViewModel()
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val cardColor by viewModel.cardColor.collectAsState(Color(248, 248, 248))
    val borderColor by viewModel.borderColor.collectAsState(Color(248, 248, 248))
    val borderWidth by viewModel.borderWidth.collectAsState(0)
    val elevation by viewModel.elevation.collectAsState(0)

    Timber.d("cardColor $cardColor")

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Cards", leftIconBtn = {
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
                    .fillMaxSize().background(MaterialTheme.colors.background),
                contentPadding = PaddingValues(16.dp)
            ) {

                item {

                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Background color",
                        selectedColor = cardColor,
                        onNewColorSelected = {
                            viewModel.setCardColor(it)
                        })

                }
                item {

                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Border color",
                        selectedColor = borderColor,
                        onNewColorSelected = {
                            viewModel.setBorderColor(it)
                        })

                }

                item {


                    SliderCardItem(
                        modifier = itemModifier,
                        text = "Border width",
                        value = borderWidth.toFloat(),
                        steps = 25,
                        valueRange = 0f..25f,
                        onChange = {
                            viewModel.setBorderWidth(it.toInt())
                        })
                }

                item {

                    SliderCardItem(
                        modifier = itemModifier,
                        text = "Elevation",
                        value = elevation.toFloat(),
                        steps = 25,
                        valueRange = 0f..25f,
                        onChange = {
                            viewModel.setElevation(it.toInt())
                        })

                }

            }
        }
    }

}
