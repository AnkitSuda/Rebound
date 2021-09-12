package com.ankitsuda.rebound.ui.screens.personalization.card

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
    val borderEnabled by viewModel.borderEnabled.collectAsState(false)

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
    ) {

        val itemModifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)

        with(LocalDialog.current) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {

                item {

                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Background color",
                        selectedColor = cardColor,
                        onClick = {
                            dialogContent = {
                                ColorPicker(onColorSelected = {
                                    viewModel.setCardColor(it)
                                })
                            }
                            showDialog()
                        })

                }

                item {

                    SwitchCardItem(
                        modifier = itemModifier,
                        text = "Border enabled",
                    checked = borderEnabled,
                    onChange = {
                        viewModel.setBorderEnabled(it)
                    })

                }

            }
        }
    }

}
