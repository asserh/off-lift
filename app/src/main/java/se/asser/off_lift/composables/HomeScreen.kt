package se.asser.off_lift.composables

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.atStartOfMonth
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onFloatingActionButtonClick: MutableState<(() -> Unit)?>,
) {
    onFloatingActionButtonClick.value = {
        println("FloatingActionButton clicked!")
    }
    val coroutineScope = rememberCoroutineScope()
    var selectedDate by remember { mutableStateOf<LocalDate>(LocalDate.now()) }

    val currentYear = Year.now()
    val startDate = currentYear.atMonth(1).atStartOfMonth()
    val endDate = currentYear.atMonth(12).atEndOfMonth()
    val currentDate = LocalDate.now()
    val numberOfDays = currentYear.length()

    val calendarState = rememberWeekCalendarState(startDate, endDate, currentDate)

    val pagerState =
        rememberPagerState(
            initialPage = ChronoUnit.DAYS.between(startDate, currentDate).toInt()
        ) { numberOfDays }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            Log.d("Page change", "Page changed to $page")
            selectedDate = startDate.plusDays(page.toLong())
            calendarState.scrollToWeek(selectedDate)
        }
    }

    Column {
        WeekCalendar(
            state = calendarState,
            weekHeader = { week ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            calendarState.scrollToWeek(week.days.last().date.minusWeeks(1))
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous week",
                            tint = Color.Gray
                        )
                    }
                    Text(
                        text = week.days.first().date.month.name
                    )
                    IconButton(onClick = {
                        coroutineScope.launch {
                            calendarState.scrollToWeek(week.days.last().date.plusWeeks(1))
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next week",
                            tint = Color.Gray
                        )
                    }

                }
            },
            dayContent = { day ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(
                            color = if (day.date == selectedDate) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            } else {
                                Color.Transparent
                            }
                        )
                        .clickable {
                            selectedDate = day.date
                            coroutineScope.launch {
                                pagerState.scrollToPage(
                                    ChronoUnit.DAYS
                                        .between(startDate, day.date)
                                        .toInt()
                                )
                            }
                        }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = day.date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = day.date.dayOfWeek.name.first().toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            },
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text("Selected date is: $selectedDate")
        }
    }
}
