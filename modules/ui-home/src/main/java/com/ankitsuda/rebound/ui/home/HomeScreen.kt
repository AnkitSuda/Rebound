package com.ankitsuda.rebound.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ankitsuda.rebound.ui.components.TopBar

@Composable
fun HomeScreen() {

    Surface(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
            TopBar(title = "Home")
        }
    }


}