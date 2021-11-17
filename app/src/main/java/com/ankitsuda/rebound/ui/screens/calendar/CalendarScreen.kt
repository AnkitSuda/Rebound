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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.ankitsuda.base.util.MonthItem
import com.ankitsuda.ui.components.InfiniteListHandler
import com.ankitsuda.ui.components.TopBar
import com.ankitsuda.ui.components.TopBarBackIconButton
import com.ankitsuda.ui.components.TopBarIconButton
import com.ankitsuda.ui.components.calendar.CalendarMonthItem
import com.ankitsuda.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.base.util.CalendarDate
import com.ankitsuda.base.util.CalendarItem
import com.ankitsuda.base.util.CalendarUtils
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

@Composable
fun CalendarScreen(
    navController: NavController,
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
            TopBar(title = "Calendar", strictLeftIconAlignToStart = true, leftIconBtn = {
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
