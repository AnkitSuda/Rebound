package com.ankitsuda.rebound.ui.components.calpose.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter

@Composable
internal fun DefaultMonthTitle(
    month: YearMonth,
    isCurrentMonth:Boolean = false,
    textStyle: TextStyle = TextStyle()
){
    val title = remember(month){
        val formatter = DateTimeFormatter.ofPattern("MMMM  yyyy")
        month.format(formatter)
    }

    Text(
        text = title,
        modifier = Modifier.padding(vertical = 8.dp),
        style = TextStyle(
            fontWeight = if (isCurrentMonth) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = 22.sp,
        ).merge(textStyle)
    )
}

@Preview("NonCurrentMonth",widthDp = 200,heightDp = 40)
@Composable
fun NonCurrentMonthPreview(){
    DefaultMonthTitle(month = YearMonth.of(2020,10), isCurrentMonth = false)
}

@Preview("CurrentMonth",widthDp = 200,heightDp = 40)
@Composable
fun CurrentMonthPreview(){
    DefaultMonthTitle(month = YearMonth.of(2020,8), isCurrentMonth = true)
}