package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight

/**
 * TopBar, usage as a toolbar
 * @param title Title of the screen
 */
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    statusBarEnabled: Boolean = true,
    leftIconBtn: (@Composable () -> Unit)? = null,
    rightIconBtn: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Status bar
        if (statusBarEnabled) {
            Box(modifier = Modifier.statusBarsHeight())
        }

        // Main TopBar content
        Box(
            modifier = Modifier
                .height(56.dp)
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxWidth(),
        ) {
            leftIconBtn?.let {
                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    it()
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.align(Alignment.Center)
            )

            rightIconBtn?.let {
                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    it()
                }
            }
        }

    }
}

@Composable
fun TopBarIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(24.dp))
    }
}