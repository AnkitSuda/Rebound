package com.ankitsuda.rebound.ui.screens.history

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.entities.Workout
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.utils.CalendarDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.OffsetDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {
    private var _week: SnapshotStateList<Date> = mutableStateListOf()
    val week = _week

    val today = CalendarDate.today.date

    fun getCurrentWeek() {
        val c = Calendar.getInstance().apply {
            this.set(Calendar.HOUR_OF_DAY, 0)
            this.set(Calendar.MINUTE, 0)
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
        }
        val tempList = arrayListOf<Date>()
        c[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        for (i in 0..6) {
            tempList.add(c.time)
            c.add(Calendar.DATE, 1)
        }
//        tempList.removeAt(0)
        week.clear()
        week.addAll(tempList)
    }

    fun getWorkoutsOnDate(date: Date): Flow<List<Workout>> =
        workoutsRepository.getAllWorkoutsOnDate(date)

}