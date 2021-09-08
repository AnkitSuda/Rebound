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
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.calendar.CalendarDayItem
import com.ankitsuda.rebound.ui.components.calendar.CalendarMonthItem
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
