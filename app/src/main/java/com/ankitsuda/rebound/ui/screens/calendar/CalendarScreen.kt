package com.ankitsuda.rebound.ui.screens.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.calendar.CalendarMonthItem
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.utils.CalendarDate
import com.ankitsuda.rebound.utils.CalendarUtils
import com.ankitsuda.rebound.utils.MonthItem
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onDateSelected: (selectedDate: CalendarDate) -> Unit,
    viewModel: CalendarScreenViewModel = hiltViewModel()
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val scrollState = rememberLazyListState()

    val monthFormatter = SimpleDateFormat(CalendarUtils.MONTH_FORMAT, Locale.getDefault())
    val dayFormatter = SimpleDateFormat(CalendarUtils.DAY_FORMAT, Locale.getDefault())

    val calendar = viewModel.calendar

    LaunchedEffect(key1 = Unit) {
        viewModel.getCalendar()
    }

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Calendar", leftIconBtn = {
                TopBarBackIconButton {
                    onBackClick()
                }
            })
        },
        modifier = modifier
    ) {
        LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
            items(calendar.size) {
                val month = calendar[it] as MonthItem
                CalendarMonthItem(month = month, month.days, onClickOnDay = { dateItem ->
                    onDateSelected(dateItem.date)
                })
            }
        }
    }
}
