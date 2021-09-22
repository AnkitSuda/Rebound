package com.ankitsuda.rebound.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Today
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.ankitsuda.rebound.ui.components.InfiniteListHandler
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.components.calendar.CalendarMonthItem
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.utils.CalendarDate
import com.ankitsuda.rebound.utils.CalendarItem
import com.ankitsuda.rebound.utils.CalendarUtils
import com.ankitsuda.rebound.utils.MonthItem
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

@Composable
fun CalendarScreen(
    navController: NavHostController,
    viewModel: CalendarScreenViewModel = hiltViewModel()
) {
    val selectedDate =
        navController.currentBackStackEntry?.arguments?.getString("selectedDate")?.let {
            CalendarDate(Date(it.toLong()))
        } ?: CalendarDate.today

    Timber.d("Pre selected date  ${selectedDate.date.toString()}")

    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val scrollState = rememberLazyListState()

    val monthFormatter = SimpleDateFormat(CalendarUtils.MONTH_FORMAT, Locale.getDefault())
    val dayFormatter = SimpleDateFormat(CalendarUtils.DAY_FORMAT, Locale.getDefault())

    val calendar = viewModel.calendar
    val today = CalendarDate.today

    val coroutine = rememberCoroutineScope()


    LaunchedEffect(key1 = Unit) {
        if (calendar.isEmpty()) {
            viewModel.getCalendar()
            try {
                scrollState.scrollToItem(calendar.indexOf((calendar.filter {
                    try {
                        (it as MonthItem).date.month == selectedDate.month && (it as MonthItem).date.year == selectedDate.year
                    } catch (e1: Exception) {
                        (it as MonthItem).date.month == today.month && (it as MonthItem).date.year == today.year
                    }
                }[0])))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Calendar", leftIconBtn = {
                TopBarIconButton(icon = Icons.Outlined.Close, title = "Close calendar") {
                    navController.popBackStack()
                }
            }, rightIconBtn = {
                TopBarIconButton(
                    icon = Icons.Outlined.Today,
                    title = "Jump to today",
                    onClick = {
                        coroutine.launch {
                            scrollState.animateScrollToItem(calendar.indexOf((calendar.filter {
                                (it as MonthItem).date.month == today.month && (it as MonthItem).date.year == today.year
                            }[0]) as CalendarItem))
                        }
                    })
            })
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {
        LazyColumn(state = scrollState, modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {
            items(calendar.size) {
                val month = calendar[it] as MonthItem
                CalendarMonthItem(
                    month = month,
                    days = month.days,
                    selectedDate = selectedDate,
                    onClickOnDay = { dateItem ->
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "date",
                            dateItem.date.date.time
                        )
                        navController.popBackStack()
                    })
            }
        }

    }
}
