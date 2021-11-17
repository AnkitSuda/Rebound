package com.ankitsuda.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.ui.theme.ReboundTheme

/**
 * This card matches look and feel the app and user choices
 * @param content card body
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ReboundTheme.colors.card,
    content: @Composable () -> Unit
) {

    val border = if (ReboundTheme.dimens.cardBorderWidth == 0.dp) null else BorderStroke(
        width = ReboundTheme.dimens.cardBorderWidth,
        color = ReboundTheme.colors.cardBorder
    )
    Card(
        modifier = modifier,
        elevation = ReboundTheme.dimens.cardElevation,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = backgroundColor,
        border = border,
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
    backgroundColor: Color = ReboundTheme.colors.card,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {

    val border = if (ReboundTheme.dimens.cardBorderWidth == 0.dp) null else BorderStroke(
        width = ReboundTheme.dimens.cardBorderWidth,
        color = ReboundTheme.colors.cardBorder
    )
    Card(
        modifier = modifier,
        elevation = ReboundTheme.dimens.cardElevation,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = backgroundColor,
        border = border,
        onClick = onClick,
        content = content
    )
}