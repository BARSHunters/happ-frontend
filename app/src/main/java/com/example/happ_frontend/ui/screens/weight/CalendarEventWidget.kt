package com.example.happ_frontend.ui.screens.weight

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.happ_frontend.ui.domain.login_register.now
import com.example.happ_frontend.model.weight.CalendarEvent
import java.time.LocalDate

private const val CALENDAR_MAX_DAYS_DEPTH = 3

/**
 * A composable function that displays a calendar view with events and allows users to add new events.
 *
 * @param events A list of [CalendarEvent] objects to be displayed in the calendar.
 * @param windowCount The count of windows in the calendar picker.
 * @param todayDate The current date.
 * @param selectedDate The date currently selected in the calendar.
 * @param onDateSelected A callback function that is invoked when a date is selected in the calendar.
 * @param groupEventsByDate A flag indicating whether events should be grouped by date.
 * @param eventValueFormatter A composable function that formats the event values.
 * @param visualizeAvailable A flag indicating whether to show the visualization button.
 * @param onEventAddButtonClicked A callback function invoked when the add event button is clicked.
 * @param onVisualizeButtonClicked A callback function invoked upon clicking the visualization button.
 * @param labelTextForAddEventButton The text to display on the add event button.
 * @param labelTextForVisualizeButton The text to display on the visualization button.
 * @param visualizeEnabledCondition Condition that determines if the visualization button is clickable.
 * @author Vad1mChK
 */
@Composable
fun <T: Any> CalendarEventWidget(
    events: List<CalendarEvent<T>>,
    windowCount: Int = 5, // Count of windows in calendar picker
    todayDate: LocalDate = LocalDate.now(),
    selectedDate: LocalDate = todayDate,
    onDateSelected: (LocalDate) -> Unit = { _ -> },
    groupEventsByDate: Boolean = false,
    eventValueFormatter: @Composable (T) -> String = { value: T -> value.toString() }, // Formatting function for event values
    visualizeAvailable: Boolean = false,
    onEventAddButtonClicked: () -> Unit = { },
    onVisualizeButtonClicked: () -> Unit = { },
    labelTextForAddEventButton: String = "",
    labelTextForVisualizeButton: String = "",
    visualizeEnabledCondition: () -> Boolean = { true }
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
                it.dateTime.toLocalDate() <= selectedDate &&
                it.dateTime.toLocalDate() > selectedDate.minusDays(CALENDAR_MAX_DAYS_DEPTH.toLong())
            },
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

    if (visualizeAvailable) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onVisualizeButtonClicked,
            enabled = visualizeEnabledCondition(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text(labelTextForVisualizeButton)
        }
    }
}