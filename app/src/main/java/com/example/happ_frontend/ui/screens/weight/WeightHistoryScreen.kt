package com.example.happ_frontend.ui.screens.weight

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.example.happ_frontend.ui.AppViewModelProvider
import com.example.happ_frontend.ui.domain.login_register.AuthState
import com.example.happ_frontend.ui.domain.weight.WeightHistoryFormData
import com.example.happ_frontend.ui.domain.weight.minus
import com.example.happ_frontend.ui.domain.weight.plus
import com.example.happ_frontend.ui.screens.home.HealthCategoryWidget
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.abs

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
    viewModel: WeightHistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val weightHistoryState by viewModel.weightHistoryState.collectAsState()

    var date by remember { mutableStateOf(LocalDate.now()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchWeightHistoryFromServer()
    }

    LaunchedEffect(viewModel.weightHistoryState) {
        val state = weightHistoryState
        if (state is WeightHistoryViewModel.WeightHistoryState.Failure) {
            Toast.makeText(
                context,
                "Error: ${state.message}", Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(LocalDate.now()) {
        println("Updating date...")
        date = LocalDate.now()
    }

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

            when (val state = weightHistoryState) {
                WeightHistoryViewModel.WeightHistoryState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 64.dp, horizontal = 32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                WeightHistoryViewModel.WeightHistoryState.Success -> {
                    CalendarEventWidget(
                        events = viewModel.weightEvents.filter { !it.prediction },
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
                        visualizeEnabledCondition = {
                            viewModel.weightEvents
                                .filter { !it.prediction }
                                .size >= 2
                        }
                    )
                }

                is WeightHistoryViewModel.WeightHistoryState.Failure -> @Composable {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 64.dp, horizontal = 32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(
                                R.string.health_category_weight_widget_failure,
                                state.message
                            )
                        )
                        Button(onClick = { viewModel.fetchWeightHistoryFromServer() }) {
                            Text(stringResource(R.string.health_category_weight_widget_reload))
                        }
                    }
                }
            }
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

//                val newEvent = WeightCalendarEvent(
//                    uiState.entryDate.atTime(uiState.entryTime),
//                    value = weightKg.kg,
//                )
//
//                Log.d(
//                    "WeightHistoryScreen, WeightAddEventDialog",
//                    "Added new weight event: $newEvent"
//                )

                viewModel.addWeightHistoryEvent(weightKg.toFloat())

//                when (viewModel.weightEvents.size) {
//                    0, 1 -> {}
//                    else -> {
//                        val sortedWeightEvents = viewModel.weightEvents.s
//                        val (firstX, firstY) = viewModel.weightEvents.first()
//                        val (lastX, lastY) = viewModel.weightEvents.last()
//                        viewModel.setAllPredictedWeightEvents(listOf(
//                            WeightCalendarEvent(
//                                dateTime = (
//                                        lastX.toKotlinLocalDateTime() +
//                                                (lastX.toKotlinLocalDateTime() - firstX.toKotlinLocalDateTime())
//                                        ).toJavaLocalDateTime(),
//                                value = lastY + (lastY - firstY),
//                            )
//                        ))
//                    }
//                }

                viewModel.formShown = false
            }
        )
    }

    if (viewModel.visualizeShown) {
        VisualizeDialog(
            stringResource(R.string.health_category_weight_prediction),
            onClose = { viewModel.visualizeShown = false }
        ) {
            val (realWeightEvents, predictedWeightEvents) = viewModel.weightEvents
                .filter {
                    val eventDate = it.dateTime.toLocalDate().toKotlinLocalDate()
                    val currentDate = date.toKotlinLocalDate()

                    abs(eventDate.daysUntil(currentDate)) <= 7
                }
                .sortedBy { it.dateTime }
                .partition { !it.prediction }
                .let {
                    it.first.sortedBy { it.dateTime } to
                            it.second.sortedBy { it.dateTime }.toMutableList()
                }
            predictedWeightEvents.add(0, realWeightEvents.last())
            WeightPredictionChartWidget(
                eventSeries = realWeightEvents,
                predictionEventSeries = predictedWeightEvents,
            )
        }
    }
}

private fun onGoBackDefault(navigationController: NavHostController) {
    navigationController.popBackStack()
}