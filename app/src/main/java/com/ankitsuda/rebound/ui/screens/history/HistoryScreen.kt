package com.ankitsuda.rebound.ui.screens.history

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import com.google.accompanist.insets.statusBarsHeight
import timber.log.Timber
import java.util.*
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.screens.calendar.CalendarScreen
import com.ankitsuda.rebound.utils.CalendarDate
import java.text.SimpleDateFormat

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun HistoryScreen(
    navController: NavHostController,
    viewModel: HistoryScreenViewModel = hiltViewModel()
) {
    var date: Date by remember {
        mutableStateOf(
            try {
                Date(
                    navController.currentBackStackEntry!!.arguments!!.getString(
                        "date"
                    )!!.toLong()
                )
            } catch (e: Exception) {
                Timber.e(e)
                CalendarDate.today.date
            }
        )
    }
    val calendar = Calendar.getInstance().apply {
        this.time = date
        this.set(Calendar.HOUR_OF_DAY, 0)
        this.set(Calendar.MINUTE, 0)
        this.set(Calendar.SECOND, 0)
        this.set(Calendar.MILLISECOND, 0)
    }

    val isSameYear = calendar.get(Calendar.YEAR) == CalendarDate.today.year
    val isToday = calendar.time == CalendarDate.today.date
    Timber.d(date.toString())

    val dayFormatter =
        SimpleDateFormat(if (isSameYear) "EEE, MMM d" else "MMM d, yyyy", Locale.getDefault())

    val isCalendarMode by viewModel.isCalendarMode.observeAsState(false)

    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val week = remember {
        mutableStateListOf(Date())
    }
    var today by remember {
        mutableStateOf(Date())
    }

    BackHandler(enabled = isCalendarMode) {
        viewModel.toggleCalendarMode()
    }

    LaunchedEffect(key1 = Unit) {
        val c = Calendar.getInstance()
        today = c.time
        c[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        for (i in 0..6) {
            week.add(c.time)
            c.add(Calendar.DATE, 1)
        }
        week.removeAt(0)
    }

    val isWeekHeaderVisible = isToday

    Surface() {
        // History
        Crossfade(targetState = isCalendarMode) {
            if (!it) {
                Column() {
                    Box(
                        modifier = Modifier
                            .statusBarsHeight()
                            .fillMaxWidth()
                            .zIndex(2f)
                            .background(MaterialTheme.colors.surface)
                    )

                    CollapsingToolbarScaffold(
                        state = collapsingState,
                        toolbar = {
                            TopBar(
                                title = if (isToday) "Today" else dayFormatter.format(date),
                                statusBarEnabled = false,
                                leftIconBtn = {
                                    TopBarIconButton(
                                        icon = Icons.Outlined.DateRange,
                                        title = "Show calendar",
                                        onClick = {
                                            viewModel.toggleCalendarMode()
                                        }
                                    )
                                },
                                rightIconBtn = {
                                    TopBarIconButton(
                                        icon = Icons.Outlined.MoreVert,
                                        title = "Open menu",
                                        onClick = {

                                        }
                                    )
                                }
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {


                        // User routines
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {
                            // Sticky Calendar

                            if (isWeekHeaderVisible) {
                                stickyHeader {
                                    // For testing only

                                    Card(
                                        shape = RoundedCornerShape(0),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {

                                        FlowRow(
                                            mainAxisAlignment = MainAxisAlignment.SpaceEvenly,
                                            mainAxisSize = SizeMode.Expand,
                                            modifier = Modifier
                                                .background(MaterialTheme.colors.surface)
                                                .padding(start = 8.dp, end = 8.dp)
                                        ) {
                                            for (day in week) {
                                                WeekDay(
                                                    day = day,
                                                    isSelected = day == date,
                                                    isToday = false
                                                )
                                            }
                                        }


                                    }

                                }
                            }


                            items(50) {
                                Text(text = it.toString(), style = MaterialTheme.typography.caption)
                            }

                        }

                    }
                }
            } else {
                // Calendar

                CalendarScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    onBackClick = {
                        viewModel.toggleCalendarMode()
                    },
                    onDateSelected = { selectedDate ->
                        date = selectedDate.date
                        viewModel.toggleCalendarMode()
                    }
                )
            }
        }
    }

}