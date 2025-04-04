package com.example.happ_frontend.ui.screens.weight

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.domain.login_register.format
import com.example.happ_frontend.ui.domain.login_register.now
import com.example.happ_frontend.model.weight.WeightCalendarEvent
import com.example.happ_frontend.ui.domain.weight.WeightHistoryViewModel
import com.example.happ_frontend.model.weight.kg
import com.example.happ_frontend.ui.domain.weight.WeightHistoryFormData
import com.example.happ_frontend.ui.domain.weight.minus
import com.example.happ_frontend.ui.domain.weight.plus
import com.example.happ_frontend.ui.screens.home.HealthCategoryWidget
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime

/**
 * Displays a page header with a back button, a health category widget, a calendar widget for
 * selecting and viewing weight events, and a weight prediction chart.
 *
 * If the form for adding a new weight event is shown, it displays a [WeightAddEventDialog].
 *
 * @param onGoBack A function that is called when the back button is clicked.
 * @param viewModel An instance of [WeightHistoryViewModel] that provides the data and logic for this screen.
 * @author Vad1mChK
 */
@Composable
fun WeightHistoryScreen(
    navigationController: NavHostController? = null,
    onGoBack: (NavHostController) -> Unit = ::onGoBackDefault,
    viewModel: WeightHistoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var date by remember { mutableStateOf(LocalDate.now()) }

//    var throwawayEvents = (0..8).map { index ->
//        val dateTime = LocalDateTime.now()
//        WeightCalendarEvent(
//            dateTime = dateTime - DateTimePeriod(hours = 12) * index,
//            value = (0.95.pow(index) * 100).kg,
//        )
//    }
//    var throwawayPredictedEvents = (0..8).map { index ->
//        val dateTime = LocalDateTime.now()
//        WeightCalendarEvent(
//            dateTime = dateTime + DateTimePeriod(hours = 12) * index,
//            value = (1.1.pow(index) * 100).kg,
//        )
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 64.dp, horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        PageHeaderWithBackButton(
            title = stringResource(R.string.health_category_weight_page_title),
            onGoBack = { navigationController?.let(onGoBack) }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            HealthCategoryWidget(
                title = stringResource(R.string.health_category_weight_title),
                description = stringResource(R.string.health_category_weight_description),
                iconVector = Icons.Default.Accessibility,
                clickable = false,
            )
            Text(
                stringResource(R.string.health_category_weight_page_title),
                fontWeight = FontWeight.Black,
            )

            CalendarEventWidget(
                events = viewModel.weightEvents,
                selectedDate = date,
                onDateSelected = { date = it },
                groupEventsByDate = true,
                eventValueFormatter = {
                    "${it.kg.format(precision = 1)} ${
                        stringResource(R.string.unit_mass_kg)
                    }"
                },
                labelTextForAddEventButton = stringResource(R.string.health_category_weight_button_add_entry),
                onEventAddButtonClicked = {
                    viewModel.resetFormData(keepFields =
                        if (viewModel.validateAddEventDialog())
                            setOf(WeightHistoryFormData::entryWeightKgString)
                        else emptySet()
                    )
                    viewModel.formShown = true
                },
                visualizeAvailable = true,
                labelTextForVisualizeButton = stringResource(R.string.health_category_weight_button_visualize),
                onVisualizeButtonClicked = {
                    viewModel.visualizeShown = true
                },
                visualizeEnabledCondition = { viewModel.weightEvents.size >= 2 }
            )
        }
    }

    if (viewModel.formShown) {
        WeightAddEventDialog(
            onCancel = {
                viewModel.formShown = false
            },
            onSubmit = {
                val weightKg = uiState.entryWeightKgString.toBigDecimalOrNull() ?:
                    return@WeightAddEventDialog

                val newEvent = WeightCalendarEvent(
                    uiState.entryDate.atTime(uiState.entryTime),
                    value = weightKg.kg,
                )

                Log.d(
                    "WeightHistoryScreen, WeightAddEventDialog",
                    "Added new weight event: $newEvent"
                )

                viewModel.addWeightEvent(newEvent)

                // TODO Actual predictions must be retrieved from the server too
                when (viewModel.weightEvents.size) {
                    0 -> viewModel.clearPredictedWeightEvents()
                    1 -> {
                        viewModel.setAllPredictedWeightEvents(
                            listOf(
                                WeightCalendarEvent(
                                    dateTime = LocalDateTime.now() + DateTimePeriod(hours = 12),
                                    value = viewModel.weightEvents.last().value,
                                ),
                                WeightCalendarEvent(
                                    dateTime = LocalDateTime.now() + DateTimePeriod(hours = 24),
                                    value = viewModel.weightEvents.last().value,
                                )
                            )
                        )
                    }
                    else -> {
                        val (firstX, firstY) = viewModel.weightEvents.first()
                        val (lastX, lastY) = viewModel.weightEvents.last()

                        viewModel.setAllPredictedWeightEvents(listOf(
                            WeightCalendarEvent(
                                dateTime = lastX + (lastX - firstX),
                                value = lastY + (lastY - firstY),
                            )
                        ))
                    }
                }

                viewModel.formShown = false
            }
        )
    }

    if (viewModel.visualizeShown) {
        VisualizeDialog(
            stringResource(R.string.health_category_weight_prediction),
            onClose = { viewModel.visualizeShown = false }
        ) {
            WeightPredictionChartWidget(
                eventSeries = viewModel.weightEvents
                    .sortedByDescending { it.dateTime },
                predictionEventSeries = if (viewModel.weightEvents.isNotEmpty())
                    viewModel.predictedWeightEvents + viewModel.weightEvents.last()
                else viewModel.predictedWeightEvents,
            )
        }
    }
}

private fun onGoBackDefault(navigationController: NavHostController) {
    navigationController.popBackStack()
}