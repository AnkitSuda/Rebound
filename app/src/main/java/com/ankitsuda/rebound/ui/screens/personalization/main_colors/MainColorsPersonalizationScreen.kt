package com.ankitsuda.rebound.ui.screens.personalization.main_colors

import androidx.compose.foundation.background
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.rebound.data.datastore.AppPreferences
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.components.color_picker.ColorPicker
import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog
import com.ankitsuda.rebound.ui.theme.DefaultAccentColor

@Composable
fun MainColorsPersonalizationScreen(
    navController: NavController,
    viewModel: MainColorsPersonalizationScreenViewModel = hiltViewModel()
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val isLightTheme by viewModel.isLightTheme.collectAsState(initial = false)

    val primaryColor by viewModel.primaryColor.collectAsState(initial = DefaultAccentColor)
    val backgroundColor by viewModel.backgroundColor.collectAsState(initial = Color.White)

    val onPrimaryColor by viewModel.onPrimaryColor.collectAsState(initial = Color.White)
    val onBackgroundColor by viewModel.onBackgroundColor.collectAsState(initial = Color.Black)

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
                        text = "Is Light Theme",
                        checked = isLightTheme,
                        onChange = {
                            viewModel.setIsLightTheme(it)
                        }
                    )

                }
                item {

                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Primary Color",
                        selectedColor = primaryColor,
                        onNewColorSelected = {
                            viewModel.setPrimaryColor(it)
                        },
                    )

                }

                item {

                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Background Color",
                        selectedColor = backgroundColor,
                        onNewColorSelected = {
                            viewModel.setBackgroundColor(it)
                        }
                    )

                }

                item {
                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "On Primary Color",
                        selectedColor = onPrimaryColor,
                        useAltColorPicker = true,
                        onNewColorSelected = {
                            viewModel.setOnPrimaryColor(it)
                        })
                }
                item {
                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "On Background Color",
                        selectedColor = onBackgroundColor,
                        onNewColorSelected = {
                            viewModel.setOnBackgroundColor(it)
                        })
                }
            }
        }
    }

}

