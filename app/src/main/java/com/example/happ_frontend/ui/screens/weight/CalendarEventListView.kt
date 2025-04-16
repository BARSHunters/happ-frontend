package com.example.happ_frontend.ui.screens.weight

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.domain.login_register.now
import com.example.happ_frontend.model.weight.CalendarEvent
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * A composable function that displays a list of calendar events in a scrollable view.
 *
 * @param events A list of [CalendarEvent] objects to be displayed.
 * @param todayDate The current date. Default value is the current date obtained using [LocalDate.now].
 * @param eventValueFormatter A function that formats the event value to a string.
 *   Default value is a lambda that converts the event value to a string using [toString].
 * @param groupByDate A flag indicating whether to group the events by date. Default value is false.
 * @author Vad1mChK
 */
@Composable
fun <T: Any> CalendarEventListView(
    events: List<CalendarEvent<T>>,
    todayDate: LocalDate = LocalDate.now(),
    eventValueFormatter: @Composable (T) -> String = { eventValue -> eventValue.toString() },
    groupByDate: Boolean = false
) {
    if (events.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(
                R.string.health_category_common_event_list_no_data),
                textAlign = TextAlign.Center
            )
        }
    }
    else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    LocalConfiguration.current.screenHeightDp.dp / 4
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(all = 16.dp)
        ) {
            val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM - eee", Locale.getDefault())
//                LocalDate.Format {
//                dayOfMonth()
//                chars(" ")
//                monthName(MonthNames.ENGLISH_FULL)
//                chars(" - ")
//                dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
//            }

            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
//                LocalDateTime.Format {
//                hour(Padding.ZERO)
//                chars(":")
//                minute(Padding.ZERO)
//            }

            if (groupByDate) {
                items(events.groupBy { it.dateTime.toLocalDate() }.toList()) { (eventsDate, eventGroup) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        HorizontalDivider()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween
                        ) {
                            Text(
                                dateFormatter.format(eventsDate),
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                            )
                            if (eventsDate == todayDate) {
                                Text(
                                    stringResource(R.string.health_category_common_event_list_today)
                                )
                            }
                        }
                        eventGroup.forEach { event ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Absolute.SpaceBetween
                            ) {
                                Text(
                                    timeFormatter.format(event.dateTime)
                                )
                                Text(
                                    eventValueFormatter(event.value)
                                )
                            }
                        }
                    }
                }
            } else {
                items(events) { event ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        HorizontalDivider()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween
                        ) {
                            Text(
                                dateFormatter.format(event.dateTime.toLocalDate()),
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                            )
                            if (event.dateTime.toLocalDate() == todayDate) {
                                Text(
                                    stringResource(R.string.health_category_common_event_list_today)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween
                        ) {
                            Text(
                                timeFormatter.format(event.dateTime)
                            )
                            Text(
                                eventValueFormatter(event.value)
                            )
                        }
                    }
                }
            }
        }
    }
}