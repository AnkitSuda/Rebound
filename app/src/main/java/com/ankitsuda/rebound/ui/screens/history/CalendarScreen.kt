package com.ankitsuda.rebound.ui.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun CalendarScreen(navController: NavHostController) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Calendar", leftIconBtn = {
                TopBarBackIconButton {
                    navController.popBackStack()
                }
            })
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(100) { Text(text = it.toString()) }
        }
    }
}