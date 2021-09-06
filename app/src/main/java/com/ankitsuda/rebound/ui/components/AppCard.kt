package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * This card matches look and feel the app and user choices
 * @param content card body
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = 0.dp,
        backgroundColor = Color(248, 248, 248),
        content = content
    )
}

/**
 * This card matches look and feel the app and user choices,
 * User can interact with this card.
 * @param onClick callback when card is clicked
 * @param content card body
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = Color(248, 248, 248),
        onClick = onClick,
        content = content
    )
}