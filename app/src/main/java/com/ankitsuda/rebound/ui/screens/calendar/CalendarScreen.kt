package com.ankitsuda.rebound.ui.screens.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.ankitsuda.rebound.ui.components.InfiniteListHandler
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.calendar.CalendarMonthItem
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.utils.CalendarDate
import com.ankitsuda.rebound.utils.CalendarItem
import com.ankitsuda.rebound.utils.CalendarUtils
import com.ankitsuda.rebound.utils.MonthItem
import timber.log.Timber
import java.lang.Exception
import java.text.SimpleDateFormat
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


    LaunchedEffect(key1 = Unit) {
        if (calendar.isEmpty()) {
            viewModel.getCalendar()

        }
    }

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Calendar", leftIconBtn = {
                TopBarBackIconButton {
                    navController.popBackStack()
                }
            })
        },
    ) {
        LazyColumn( modifier = Modifier.fillMaxSize()) {
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

                    if (it == 0) {
                        viewModel.generatePrevCalendarItems()
                    } else if (it == calendar.size - 1) {
                        viewModel.generateNextCalendarItems()
                    }
            }
        }

    }
}
