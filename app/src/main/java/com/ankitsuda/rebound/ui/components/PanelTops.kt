package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PanelTopDragHandle(modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(top = 6.dp)) {
        Box(
            modifier = Modifier
                .height(2.dp)
                .width(32.dp)
                .align(Alignment.Center)
                .background(Color(245, 245, 245))
                .clip(RoundedCornerShape(50))
        )
    }
}

@Composable
fun PanelTopCollapsed() {
    Column(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Push", style = MaterialTheme.typography.h6)
        Text(text = "32:12m", style = MaterialTheme.typography.caption)
    }
}

@Composable
fun PanelTopExpanded(
    onCollapseBtnClicked: () -> Unit,
    onTimerBtnClicked: () -> Unit,
    onFinishBtnClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onCollapseBtnClicked) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Collapse panel"
                )
            }
            IconButton(onClick = onTimerBtnClicked) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = "Collapse panel"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onFinishBtnClicked,
                elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp)
            ) {
                Text(text = "Finish", style = MaterialTheme.typography.button)
            }
        }
    }
}