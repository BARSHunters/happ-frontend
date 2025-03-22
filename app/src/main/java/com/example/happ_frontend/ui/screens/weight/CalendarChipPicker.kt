package com.example.happ_frontend.ui.screens.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.ui.domain.login_register.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.minus
import kotlinx.datetime.plus

const val DAYS_IN_WEEK = 7

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CalendarChipPicker(
    selectedDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit = { _ -> },
    windowCount: Int = DAYS_IN_WEEK,
    windowPosition: Int = (windowCount - 1) / 2
) {
    val beginningDay = selectedDate.minus(windowPosition, DateTimeUnit.DAY)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = {
                onDateSelected(
                    selectedDate.minus(1, DateTimeUnit.DAY)
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardDoubleArrowLeft,
                contentDescription = null
            )
        }

        FlowRow(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            for (i in 0 ..< windowCount) {
                val cellDate = beginningDay.plus(
                    i, DateTimeUnit.DAY
                )
                CalendarChip(
                    date = cellDate,
                    selected = cellDate == selectedDate,
                    onClick = { onDateSelected(cellDate) }
                )
            }
        }

        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = {
                onDateSelected(
                    selectedDate.plus(1, DateTimeUnit.DAY)
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardDoubleArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun CalendarChip(
    date: LocalDate,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val dayOfWeekFormatter = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
    }

    InputChip(
        selected = selected,
        onClick = onClick,
        label = {
            Column(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 0.dp),
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