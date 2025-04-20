package com.example.happ_frontend.ui.screens.activity

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.*

@Composable
fun ActivityHistoryCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Activity History",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )

        // Find the Monday of the week for the selected date
        val mondayOfWeek = remember(selectedDate) {
            selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        }

        // Month and year display
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Month",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // Go to previous month
                        val prevMonth = selectedDate.minusMonths(1)
                        onDateSelected(prevMonth)
                    }
            )

            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next Month",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // Go to next month
                        val nextMonth = selectedDate.plusMonths(1)
                        onDateSelected(nextMonth)
                    }
            )
        }

        // Generate weekdays (Monday to Sunday)
        val weekdays = remember(mondayOfWeek) {
            (0..6).map { mondayOfWeek.plusDays(it.toLong()) }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Week",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        // Go to previous week's Monday
                        val prevMonday = mondayOfWeek.minusWeeks(1)
                        onDateSelected(prevMonday)
                    }
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(weekdays) { date ->
                    DateItem(
                        date = date,
                        isSelected = date.isEqual(selectedDate),
                        onDateSelected = onDateSelected
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next Week",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        // Go to next week's Monday
                        val nextMonday = mondayOfWeek.plusWeeks(1)
                        onDateSelected(nextMonday)
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
        Color(0xFF9D89C5) // Updated to match the screenshot's purple
    else
        MaterialTheme.colorScheme.surface

    val textColor = if (isSelected)
        Color.White
    else
        MaterialTheme.colorScheme.onSurface

    val today = LocalDate.now()
    val isToday = date.equals(today)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            // Updated shape to match the screenshot (more rectangular with rounded corners)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .width(44.dp) // Adjusted to match screenshot
            .height(70.dp)
            .clickable { onDateSelected(date) }
            .padding(4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium,
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