package com.ankitsuda.rebound.ui.screens.history

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
import com.google.accompanist.insets.statusBarsHeight
import timber.log.Timber
import java.util.*
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.rebound.ui.navigation.Route
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.calendar.WEIGHT_7DAY_WEEK
import com.ankitsuda.rebound.utils.CalendarDate
import org.threeten.bp.OffsetDateTime
import java.text.SimpleDateFormat
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryScreenViewModel = hiltViewModel()
) {
    val argumentsDate = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Long>("date")?.observeAsState()

    var date = CalendarDate.today.date

    argumentsDate?.value?.let {
        date = Date(it)
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

    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val week = viewModel.week
    var today = viewModel.today

    LaunchedEffect(key1 = Unit) {
        if (week.isEmpty()) {
            viewModel.getCurrentWeek()
        }
    }

    val workouts by viewModel.getWorkoutsOnDate(date)
        .collectAsState(initial = emptyList())

    Timber.d("workouts $workouts")

    val isWeekHeaderVisible = week.any { it == date }

    Surface() {
        // History

        Column() {
            Box(
                modifier = Modifier
                    .statusBarsHeight()
                    .fillMaxWidth()
                    .zIndex(2f)
                    .background(MaterialTheme.colors.background)
            )

            CollapsingToolbarScaffold(
                state = collapsingState,
                toolbar = {
                    TopBar(
                        elevationEnabled = false,
                        title = if (isToday) "Today" else dayFormatter.format(date),
                        statusBarEnabled = false, strictLeftIconAlignToStart = false,
                        leftIconBtn = {
                            TopBarIconButton(
                                icon = Icons.Outlined.DateRange,
                                title = "Show calendar",
                                onClick = {
                                    navController.navigate(Route.Calendar.createRoute(selectedDate = date))
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
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {


                // User routines
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                ) {
                    // Sticky Calendar

                    if (isWeekHeaderVisible) {
                        stickyHeader {
                            // For testing only

                            Card(
                                shape = RoundedCornerShape(0),
                                elevation = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Row(
                                    modifier = Modifier
                                        .background(MaterialTheme.colors.background)
//                                        .padding(start = 8.dp, end = 8.dp)
                                ) {
                                    for (day in week) {
                                        WeekDay(
                                            modifier = Modifier.weight(WEIGHT_7DAY_WEEK),
                                            day = day,
                                            isSelected = day == date,
                                            onClick = {
                                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                                    "date",
                                                    day.time
                                                )
                                            }
                                        )
                                    }
                                }


                            }

                        }
                    }


                    items(workouts.size) {
                        val workout = workouts[it]
                        HistorySessionItemCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = if (it == 0) 16.dp else 0.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                ),
                            onClick = {
                                navController.navigate(
                                    Route.Session.createRoute(
                                        Random.nextLong(
                                            0,
                                            100
                                        )
                                    )
                                )
                            },
                            title = workout.name.toString(),
                            totalExercises = 7,
                            time = "45 m", volume = "1000 kg", prs = 2
                        )
                    }

                }

            }
        }

    }

}