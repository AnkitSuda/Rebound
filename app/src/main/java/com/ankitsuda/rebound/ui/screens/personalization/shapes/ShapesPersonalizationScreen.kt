package com.ankitsuda.rebound.ui.screens.personalization.shapes

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.screens.main_screen.LocalDialog
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.theme.ShapeValues
import timber.log.Timber

@Composable
fun ShapesPersonalizationScreen(
    navController: NavHostController,
    viewModel: ShapesPersonalizationScreenViewModel = hiltViewModel()
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val smallTopStart by viewModel.smallTopStart.collectAsState(0)
    val smallTopEnd by viewModel.smallTopEnd.collectAsState(0)
    val smallBottomStart by viewModel.smallBottomStart.collectAsState(0)
    val smallBottomEnd by viewModel.smallBottomEnd.collectAsState(0)

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Shapes", leftIconBtn = {
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

                    ShapesEditorCardItem(
                        modifier = itemModifier,
                        text = "Small shape",
                        defaultValues = ShapeValues(
                            topStart = smallTopStart,
                            topEnd = smallTopEnd,
                            bottomStart = smallBottomStart,
                            bottomEnd = smallBottomEnd,
                        ),
                        onValueChange = {
                            viewModel.setSmalShapeValues(it)
                        }
                    )

                }


                item {

                    ShapesEditorCardItem(
                        modifier = itemModifier,
                        text = "Small shape",
                        defaultValues = ShapeValues(
                            topStart = smallTopStart,
                            topEnd = smallTopEnd,
                            bottomStart = smallBottomStart,
                            bottomEnd = smallBottomEnd,
                        ),
                        onValueChange = {
                            viewModel.setSmalShapeValues(it)
                        }
                    )

                }


            }
        }
    }
}
