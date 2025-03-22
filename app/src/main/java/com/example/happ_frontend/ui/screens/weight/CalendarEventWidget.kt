package com.example.happ_frontend.ui.screens.weight

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.happ_frontend.ui.domain.login_register.now
import com.example.happ_frontend.ui.domain.weight.CalendarEvent
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

private const val CALENDAR_MAX_DAYS_DEPTH = 3

@Composable
fun <T: Any> CalendarEventWidget(
    events: List<CalendarEvent<T>>,
    windowCount: Int = 5, // Count of windows in calendar picker
    todayDate: LocalDate = LocalDate.now(),
    selectedDate: LocalDate = todayDate,
    onDateSelected: (LocalDate) -> Unit = { _ -> },
    groupEventsByDate: Boolean = false,
    eventValueFormatter: @Composable (T) -> String = { value: T -> value.toString() }, // Formatting function for event values
    onEventAddButtonClicked: () -> Unit = { },
    labelTextForAddEventButton: String = "",
) {
    CalendarChipPicker(
        windowCount = windowCount,
        selectedDate = selectedDate,
        onDateSelected = onDateSelected
    )

    CalendarEventListView(
        events = events
            .sortedByDescending { it.dateTime }
            .filter {
                it.dateTime.date <= selectedDate &&
                it.dateTime.date > selectedDate.minus(CALENDAR_MAX_DAYS_DEPTH, DateTimeUnit.DAY)
            },
        selectedDate = selectedDate,
        todayDate = todayDate,
        groupByDate = groupEventsByDate,
        eventValueFormatter = eventValueFormatter
    )

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onEventAddButtonClicked
    ) {
        Text(labelTextForAddEventButton)
    }
}