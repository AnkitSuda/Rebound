package com.ankitsuda.rebound.ui.screens.history

import android.R
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*
import android.widget.ArrayAdapter
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import com.ankitsuda.rebound.ui.Route
import com.ankitsuda.rebound.ui.components.*
import java.text.DateFormat
import java.text.SimpleDateFormat


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(navController: NavHostController) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val week = remember {
        mutableStateListOf(Date())
    }
    var today by remember {
        mutableStateOf(Date())
    }

    Timber.d("Today $today")

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
                    title = "History",
                    statusBarEnabled = false,
                    leftIconBtn = {
                        TopBarIconButton(
                            icon = Icons.Outlined.DateRange,
                            title = "Show calendar",
                            onClick = {
                                navController.navigate(Route.Calendar.route)
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
                stickyHeader {
                    // For testing only

                    Card(shape = RoundedCornerShape(0), modifier = Modifier.fillMaxWidth()) {

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
                                    isSelected = day == today,
                                    isToday = false
                                )
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
}