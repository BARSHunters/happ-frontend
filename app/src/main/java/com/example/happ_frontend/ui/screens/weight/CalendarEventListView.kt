package com.example.happ_frontend.ui.screens.weight

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
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
import com.example.happ_frontend.ui.domain.weight.CalendarEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding

@Composable
fun <T: Any> CalendarEventListView(
    events: List<CalendarEvent<T>>,
    selectedDate: LocalDate,
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
                ),
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
        ) {
            val dateFormatter = LocalDate.Format {
                dayOfMonth()
                chars(" ")
                monthName(MonthNames.ENGLISH_FULL)
                chars(" - ")
                dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
            }

            val timeFormatter = LocalDateTime.Format {
                hour(Padding.ZERO)
                chars(":")
                minute(Padding.ZERO)
            }

            if (groupByDate) {
                items(events.groupBy { it.dateTime.date }.toList()) { (eventsDate, eventGroup) ->
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
                                dateFormatter.format(event.dateTime.date),
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                            )
                            if (event.dateTime.date == todayDate) {
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