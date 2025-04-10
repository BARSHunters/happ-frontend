package com.example.happ_frontend.ui.screens.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.happ_frontend.ui.domain.login_register.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

const val DAYS_IN_WEEK = 7

/**
 * A composable function that displays a row of [InputChip]s representing a calendar slice.
 * Each chip represents a day of the week and displays the day number and its abbreviated weekday.
 *
 * @param selectedDate The currently selected date. Default is the current date.
 * @param onDateSelected A callback function that is invoked when a date is selected.
 * @param windowCount The number of days to display in the calendar. Default is [DAYS_IN_WEEK].
 * @param windowPosition The position of the selected date in the calendar. Default is half of [windowCount].
 * @author Vad1mChK
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CalendarChipPicker(
    selectedDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit = { _ -> },
    windowCount: Int = DAYS_IN_WEEK,
    windowPosition: Int = (windowCount - 1) / 2
) {
    // Track calculated window size to prevent changes on resize
    var calculatedWindowCount by remember { mutableIntStateOf(windowCount) }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val chipWidth = 40.dp
        val spacing = 4.dp
        val totalAvailableWidth = maxWidth - 2 * 48.dp // Account for arrow buttons

        val newWindowCount = ((totalAvailableWidth + spacing) / (chipWidth + spacing))
            .toInt()
            .coerceAtLeast(1)

        if (calculatedWindowCount == windowCount) {
            calculatedWindowCount = newWindowCount
        }

        val adjustedWindowPosition = (calculatedWindowCount - 1) / 2
        val beginningDay = selectedDate.minusDays(adjustedWindowPosition.toLong())

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                modifier = Modifier.size(48.dp),
                onClick = { onDateSelected(selectedDate.minusDays(1L)) }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardDoubleArrowLeft,
                    contentDescription = null
                )
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(calculatedWindowCount) { i ->
                    val cellDate = beginningDay.plusDays(i.toLong())
                    CalendarChip(
                        modifier = Modifier.width(chipWidth),
                        date = cellDate,
                        selected = cellDate == selectedDate,
                        onClick = { onDateSelected(cellDate) }
                    )
                    if (i != calculatedWindowCount - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            IconButton(
                modifier = Modifier.size(48.dp),
                onClick = { onDateSelected(selectedDate.plusDays(1L)) }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardDoubleArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun CalendarChip(
    modifier: Modifier,
    date: LocalDate,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val dayOfWeekFormatter = DateTimeFormatter.ofPattern("eee", Locale.getDefault())

    InputChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        label = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold
                )
                Text(
                    dayOfWeekFormatter.format(date).uppercase(),
                    style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}