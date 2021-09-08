package com.ankitsuda.rebound.ui.components.calpose.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowLeft
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.R
import com.ankitsuda.rebound.ui.components.calpose.model.CalposeActions
import org.threeten.bp.YearMonth

@Composable
fun DefaultHeader(
    month: YearMonth,
    todayMonth: YearMonth,
    actions: CalposeActions
) {
    val isCurrentMonth = todayMonth == month
    Row {
        IconButton(
            onClick = { actions.onClickedPreviousMonth() },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(imageVector = Icons.Outlined.ArrowLeft, contentDescription = "Left")
        }

        Spacer(modifier = Modifier.weight(1f))


        DefaultMonthTitle(month = month,isCurrentMonth = isCurrentMonth)

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { actions.onClickedNextMonth() },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(imageVector = Icons.Outlined.ArrowRight, contentDescription = "Right")
        }
    }
}

@Composable
fun DefaultDay(
    text: String,
    modifier: Modifier = Modifier.padding(4.dp),
    style: TextStyle = TextStyle()
) {
    Text(
        text,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = style
    )
}