package com.ankitsuda.rebound.ui.components.calpose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowLeft
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.rebound.R
import com.ankitsuda.rebound.ui.components.calpose.model.CalposeActions
import org.threeten.bp.YearMonth

@Composable
fun MaterialHeader(
    month: YearMonth,
    todayMonth: YearMonth,
    actions: CalposeActions,
    backgroundColor: Color,
    titleContainer: @Composable (@Composable () -> Unit) -> Unit = { it() }
) {
    val isCurrentMonth = todayMonth == month
    Row(
        modifier = Modifier.background(color = backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { actions.onClickedPreviousMonth() },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowLeft,
                tint = Color.White,
                contentDescription = "Left"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        titleContainer {
            DefaultMonthTitle(
                month = month,
                isCurrentMonth = isCurrentMonth,
                textStyle = TextStyle(fontSize = 22.sp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { actions.onClickedNextMonth() },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowRight,
                tint = Color.White,
                contentDescription = "Right"
            )
        }
    }
}