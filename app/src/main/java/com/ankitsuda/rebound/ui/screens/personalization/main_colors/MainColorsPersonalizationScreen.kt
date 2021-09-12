package com.ankitsuda.rebound.ui.screens.personalization.main_colors

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ankitsuda.rebound.data.datastore.AppPreferences
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.components.color_picker.ColorPicker
import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog

@Composable
fun MainColorsPersonalizationScreen(navController: NavController) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    var newPrimaryColor by remember {
        mutableStateOf(Color.Blue)
    }

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Main Colors", leftIconBtn = {
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
                        text = "Primary Color",
                        selectedColor = newPrimaryColor,
                        onNewColorSelected = {

                        },
                        onClick = {
                            dialogContent = {
                                ColorPicker(onColorSelected = {
                                    newPrimaryColor = it
                                })
                            }
                            showDialog()
                        })

                }
                item {

                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Background Color",
                        selectedColor = MaterialTheme.colors.background,
                    )

                }
                item {

                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Card Color",
                        selectedColor = MaterialTheme.colors.background,
                        onClick = {
                            dialogContent = {
                                ColorPicker(onColorSelected = {

                                })
                            }
                            showDialog()
                        }
                    )

                }
                item {
                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "On Primary Color",
                        selectedColor = MaterialTheme.colors.onPrimary,
                        onNewColorSelected = {

                        })
                }
                item {
                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "On Background Color",
                        selectedColor = MaterialTheme.colors.onBackground,
                        onNewColorSelected = {

                        })
                }
            }
        }
    }

}

