package com.example.happ_frontend.ui.screens.nutrition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun MealHistoryCalendar(
    onDateSelected: (LocalDate) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Meal History",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        var currentWeekStart by remember { mutableStateOf(LocalDate.now().minusDays(3)) }
        val dates = generateDateRange(currentWeekStart, currentWeekStart.plusDays(6))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        currentWeekStart = currentWeekStart.minusDays(7)
                    }
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(dates) { date ->
                    DateItem(
                        date = date,
                        isSelected = date == selectedDate,
                        onDateSelected = {
                            selectedDate = it
                            onDateSelected(it)
                        }
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        currentWeekStart = currentWeekStart.plusDays(7)
                    }
            )
        }
    }
}

@Composable
fun DateItem(
    date: LocalDate,
    isSelected: Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surface

    val textColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .width(60.dp)
            .height(70.dp)
            .clickable { onDateSelected(date) }
            .padding(4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Text(
            text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                .uppercase(),
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

private fun generateDateRange(start: LocalDate, end: LocalDate): List<LocalDate> {
    val dateList = mutableListOf<LocalDate>()
    var current = start
    while (!current.isAfter(end)) {
        dateList.add(current)
        current = current.plusDays(1)
    }
    return dateList
}