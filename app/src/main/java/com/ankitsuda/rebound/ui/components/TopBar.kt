package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight

/**
 * TopBar, usage as a toolbar
 * @param title Title of the screen
 */
@Composable
fun TopBar(modifier: Modifier = Modifier, title: String) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Status bar
        Box(modifier = Modifier.statusBarsHeight())

        // Main TopBar content
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }
}