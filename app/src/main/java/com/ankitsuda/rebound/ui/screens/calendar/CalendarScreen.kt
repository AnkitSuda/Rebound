package com.ankitsuda.rebound.ui.screens.calendar

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.calpose.model.CalposeActions
import com.ankitsuda.rebound.ui.components.calpose.model.CalposeWidgets
import com.ankitsuda.rebound.ui.components.calpose.widgets.DefaultDay
import com.ankitsuda.rebound.ui.components.calpose.widgets.DefaultHeader
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.calendar.CalendarDayItem
import com.ankitsuda.rebound.ui.components.calendar.CalendarMonthItem
import com.ankitsuda.rebound.ui.components.calendar.CalendarWeekRow
import com.ankitsuda.rebound.ui.components.calpose.Calpose
import com.ankitsuda.rebound.ui.components.calpose.WEIGHT_7DAY_WEEK
import com.ankitsuda.rebound.ui.components.calpose.model.CalposeDate
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.utils.CalendarDate
import com.ankitsuda.rebound.utils.CalendarUtils
import com.ankitsuda.rebound.utils.DateItem
import com.ankitsuda.rebound.utils.MonthItem
import org.threeten.bp.DayOfWeek
import org.threeten.bp.Year
import org.threeten.bp.YearMonth
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarScreen(
    navController: NavHostController,
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
                    navController.popBackStack()
                }
            })
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
            items(calendar.size) {
                val month = calendar[it] as MonthItem
                val days = viewModel.getMonth(month.date.year, month.date.month)
                CalendarMonthItem(month = month, days)
            }
        }
    }
}


@SuppressLint("NewApi")
@Composable
fun DefaultPreview() {

    var month by remember { mutableStateOf(YearMonth.now()) }
    DefaultCalendar(
        month = month,
        actions = CalposeActions(
            onClickedPreviousMonth = { month = month.minusMonths(1) },
            onClickedNextMonth = { month = month.plusMonths(1) }
        )
    )
}

@SuppressLint("NewApi")
@Composable
fun DefaultCalendar(
    month: YearMonth,
    actions: CalposeActions
) {
    Calpose(
        month = month,
        actions = actions,
        widgets = CalposeWidgets(
            header = { month, todayMonth, actions ->
                DefaultHeader(month, todayMonth, actions)
            },
            headerDayRow = { headerDayList ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(bottom = 16.dp),
                ) {
                    headerDayList.forEach {
                        DefaultDay(
                            text = it.name.first().toString(),
                            modifier = Modifier.weight(WEIGHT_7DAY_WEEK),
                            style = TextStyle(color = Color.Gray)
                        )
                    }
                }
            },
            day = { dayDate, todayDate ->
                val isToday = dayDate == todayDate
                val dayHasPassed = dayDate.day < todayDate.day
                val isCurrentMonth = dayDate.month == todayDate.month

                val widget: @Composable () -> Unit = {
                    val weight = if (isToday) 1f else WEIGHT_7DAY_WEEK
                    DefaultDay(
                        text = dayDate.day.toString(),
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(weight)
                            .fillMaxWidth(),
                        style = TextStyle(
                            color = when {
                                isCurrentMonth && dayHasPassed -> Color.Gray
                                isToday -> Color.White
                                else -> Color.Black
                            },
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }

                if (isToday) {
                    Column(
                        modifier = Modifier.weight(WEIGHT_7DAY_WEEK),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.primary)
                        ) {
                            widget()
                        }
                    }
                } else widget()
            },
            priorMonthDay = { dayDate ->
                DefaultDay(
                    text = dayDate.day.toString(),
                    style = TextStyle(color = Color.Gray),
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .weight(WEIGHT_7DAY_WEEK)
                )
            },
            headerContainer = { header ->
                Card {
                    header()
                }
            },
        )
    )
}