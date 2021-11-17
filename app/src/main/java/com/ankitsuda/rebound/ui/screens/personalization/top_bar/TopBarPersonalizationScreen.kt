package com.ankitsuda.rebound.ui.screens.personalization.top_bar

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.components.settings.RadioGroupCardItem
import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog
import com.ankitsuda.rebound.ui.screens.personalization.card.CardPersonalizationScreenViewModel
import timber.log.Timber

@Composable
fun TopBarPersonalizationScreen(
    navController: NavController,
    viewModel: TopBarPersonalizationScreenViewModel = hiltViewModel()
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val titleAlignment by viewModel.titleAlignment.collectAsState("center")
    val backgroundColor by viewModel.backgroundColor.collectAsState(Color.White)
    val contentColor by viewModel.contentColor.collectAsState(Color.White)
    val elevation by viewModel.elevation.collectAsState(0)

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Top Bar", strictLeftIconAlignToStart = true,leftIconBtn = {
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
                        text = "Title alignment",
                        onSelectionChange = { _, value ->
                            viewModel.setTitleAlignment(value)
                        },
                        items = viewModel.allTitleAlignments,
                        selected = titleAlignment
                    )
                }

                item {
                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Background color",
                        selectedColor = backgroundColor,
                        onNewColorSelected = {
                            viewModel.setBackgroundColor(it)
                        })

                }

                item {
                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Content color",
                        selectedColor = contentColor,
                        onNewColorSelected = {
                            viewModel.setContentColor(it)
                        })

                }

                item {

                    SliderCardItem(
                        modifier = itemModifier,
                        text = "Elevation",
                        value = elevation,
                        steps = 50,
                        valueRange = 0f..50f,
                        onChange = {
                            viewModel.setElevation(it)
                        }
                    )

                }

            }
        }
    }

}
