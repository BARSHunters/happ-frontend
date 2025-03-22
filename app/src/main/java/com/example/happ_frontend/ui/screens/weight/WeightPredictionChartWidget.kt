package com.example.happ_frontend.ui.screens.weight

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.domain.login_register.format
import com.example.happ_frontend.model.weight.CalendarEvent
import com.example.happ_frontend.model.weight.Mass
import com.example.happ_frontend.ui.domain.weight.fromEpochMilliseconds
import com.example.happ_frontend.ui.domain.weight.toEpochMilliseconds
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.continuous
import com.patrykandpatrick.vico.compose.cartesian.layer.dashed
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.Padding

private const val EXTRAPOLATION_RATE = 10
private const val OUTSTRETCH_RATE = 1.1
private const val EXTRA_OUTSTRETCH = 0.1

/**
 *
 * @param eventSeries The series of real weight events to be displayed on the chart.
 * @param predictionEventSeries The series of predicted weight events to be displayed on the chart.
 *
 * The function builds a line chart using the Vico library to display the weight data.
 * It first prepares the extended data for the chart, including extrapolation for missing data points.
 * Then, it configures the line chart layers, axes, and model producer for the chart.
 * Finally, it builds and hosts the chart in a Compose layout.
 *
 * The chart displays real weight events as solid lines and predicted weight events as dashed lines.
 * It also includes vertical and horizontal rulers to help visualize the data points.
 *
 * The chart is responsive and adjusts its size based on the available space.
 * It also includes axis labels and a title to provide context and clarity to the user.
 *
 * @author Vad1mChK
*/

@Composable
fun WeightPredictionChartWidget(
    eventSeries: List<CalendarEvent<Mass>>,
    predictionEventSeries: List<CalendarEvent<Mass>> = emptyList()
) {
    val dateTimeFormat = remember {
        LocalDateTime.Format {
            dayOfMonth(Padding.ZERO)
            chars(".")
            monthNumber(Padding.ZERO)
//            chars("\n")
//            hour(padding = Padding.NONE)
//            chars(":")
//            minute(padding = Padding.ZERO)
        }
    }

    if (eventSeries.size <= 1) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                stringResource(R.string.health_category_weight_prediction_not_enough_data),
                textAlign = TextAlign.Center
            )
        }
    } else {
        val dataPoints = eventSeries.map { event ->
            event.dateTime.toEpochMilliseconds() to event.value.kg
        }
        val predictedDataPoints = predictionEventSeries.map { event ->
            event.dateTime.toEpochMilliseconds() to event.value.kg
        }
        CustomLineChart(
            dataPoints.sortedBy { it.first }.distinctBy { it.first },
            predictedDataPoints.sortedBy { it.first }.distinctBy { it.first },
            xValueFormatter = { value ->
                dateTimeFormat.format(LocalDateTime.fromEpochMilliseconds(value))
            }
        )
    }
}

@Composable
private fun CustomLineChart(
    dataPoints: List<Pair<Long, Float>>,
    predictedDataPoints: List<Pair<Long, Float>> = emptyList(),
    xValueFormatter: (Long) -> String = { value -> value.toString() },
    yValueFormatter: (Float) -> String = { value -> value.toString() }
) {
    // Prepare the chart model with extended data (for extrapolation)
    val extendedData = remember(dataPoints) {
        when {
            dataPoints.size >= 2 -> {
                val validPoints = dataPoints.distinctBy { it.first }
                    .sortedBy { it.first }

                // Add extrapolation only if X values differ
                if (validPoints.size < 2) emptyList() else {
                    val first = validPoints.first()
                    val last = validPoints.last()
                    val xDiff = last.first - first.first

                    buildList {
                        if (xDiff != 0L) {
                            add(
                                first.first - EXTRAPOLATION_RATE * xDiff to
                                        calculateLinearInterpolation(first, validPoints[1], first.first - EXTRAPOLATION_RATE * xDiff)
                            )
                        }
                        addAll(validPoints)
                        if (xDiff != 0L && predictedDataPoints.isEmpty()) {
                            // only draw the right hand if there is no predicted points
                            add(
                                last.first + EXTRAPOLATION_RATE * xDiff to
                                        calculateLinearInterpolation(validPoints[validPoints.lastIndex - 1], last, last.first + EXTRAPOLATION_RATE * xDiff)
                            )
                        }
                    }
                }
            }
            dataPoints.size == 1 -> {
                val (x, y) = dataPoints.first()
                if (predictedDataPoints.isEmpty()) {
                    listOf(
                        x - EXTRAPOLATION_RATE to y,
                        x to y,
                        x + EXTRAPOLATION_RATE to y
                    )
                } else {
                    listOf(
                        x - EXTRAPOLATION_RATE to y,
                        x to y,
                    )
                }
            }
            else -> emptyList()
        }.filter {
            it.second.isFinite() && !it.second.isNaN()
        }
    }

    val extendedPredictionData = remember(predictedDataPoints) {
        when(predictedDataPoints.size) {
            0 -> emptyList<Pair<Long, Float>>()
            1 -> {
                val (x, y) = predictedDataPoints.first()
                listOf(
                    x - EXTRAPOLATION_RATE to y,
                    x to y,
                    x + EXTRAPOLATION_RATE to y
                )
            }
            else -> {
                val validPoints = predictedDataPoints.distinctBy { it.first }
                    .sortedBy { it.first }

                if (validPoints.size < 2) emptyList() else {
                    val first = validPoints.first()
                    val last = validPoints.last()
                    val xDiff = last.first - first.first

                    buildList {
                        addAll(validPoints)
                        if (xDiff != 0L) {
                            add(
                                last.first + EXTRAPOLATION_RATE * xDiff to
                                        calculateLinearInterpolation(
                                            validPoints[validPoints.lastIndex - 1], last, last.first + EXTRAPOLATION_RATE * xDiff
                                        )
                            )
                        }
                    }
                }
            }
        }
    }

    val joinedDataPoints = (dataPoints + predictedDataPoints).sortedBy { it.first }

    val minX = joinedDataPoints.minOfOrNull { it.first }?.toDouble() ?: 0.0
    val maxX = joinedDataPoints.maxOfOrNull { it.first }?.toDouble() ?: 1.0
    val minY = joinedDataPoints.minOfOrNull { it.second }?.toDouble() ?: 0.0
    val maxY = joinedDataPoints.maxOfOrNull { it.second }?.toDouble() ?: 1.0

    // Create a Chart model producer and update it with the extended data
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(extendedData) {
        modelProducer.runTransaction transaction@{
            // 1. real points
            lineSeries {
                // Split extendedData into separate X and Y lists for the series
                series(
                    x = extendedData.map { it.first },
                    y = extendedData.map { it.second }
                )
            }

            // 2. prediction points
            if (extendedPredictionData.isNotEmpty()) {
                lineSeries {
                    series(
                        x = extendedPredictionData.map { it.first },
                        y = extendedPredictionData.map { it.second }
                    )
                }
            }

            dataPoints.maxByOrNull { it.first }?.let { lastRealPoint ->
                // 3. vertical ruler
                lineSeries {
                    series(
                        x = listOf(lastRealPoint.first - 1, lastRealPoint.first),
                        y = listOf(0, lastRealPoint.second)
                    )
                }

                // 4. horizontal ruler
                lineSeries {
                    series(
                        x = listOf(0, lastRealPoint.first),
                        y = listOf(lastRealPoint.second, lastRealPoint.second)
                    )
                }
            }
        }
    }

    // Axis configurations (Y-axis on left, X-axis on bottom with custom labels)
    val startAxis = VerticalAxis.rememberStart(
        valueFormatter = { _, y, _ ->
            if (y.isFinite()) y.toFloat().format(1) else "???"
        }
    )  // left Y-axis
    val bottomAxis = HorizontalAxis.rememberBottom(
        // Format X values to strings
        valueFormatter = { _, x, _ ->
            if (x.isFinite()) xValueFormatter(x.toLong()) else "???"
        }
    )

    val realLine = LineCartesianLayer.rememberLine(
        fill = LineCartesianLayer.LineFill.single(Fill(MaterialTheme.colorScheme.primary.toArgb()))
    )
    val predictedLine = LineCartesianLayer.rememberLine(
        fill = LineCartesianLayer.LineFill.single(Fill(MaterialTheme.colorScheme.primary.toArgb())),
        stroke = LineCartesianLayer.LineStroke.dashed()
    )
    val rulerLine = LineCartesianLayer.rememberLine(
        fill = LineCartesianLayer.LineFill.single(Fill(MaterialTheme.colorScheme.onBackground.toArgb())),
        stroke = LineCartesianLayer.LineStroke.continuous(
            thickness = realLine.stroke.thicknessDp.dp / 2
        )
    )

    val seriesLines = buildList {
        add(realLine)
        if (predictedDataPoints.isNotEmpty()) add(predictedLine)
        if (dataPoints.isNotEmpty()) repeat(2) {
            add(rulerLine)
        }
    }

    val rangeProvider = CartesianLayerRangeProvider.fixed(
        minX = (minX + maxX) / 2 - (maxX - minX) * OUTSTRETCH_RATE / 2 - EXTRA_OUTSTRETCH,
        maxX = (minX + maxX) / 2 + (maxX - minX) * OUTSTRETCH_RATE / 2 + EXTRA_OUTSTRETCH,
        minY = minY - EXTRA_OUTSTRETCH,
        maxY = maxY + EXTRA_OUTSTRETCH
    )

    // Configure the line chart layer with Y-axis range override and smoothing
    val realLineLayer = rememberLineCartesianLayer(
        rangeProvider = rangeProvider,
        lineProvider = LineCartesianLayer.LineProvider.series(
            realLine
        )
    )
    val predictedlineLayer = if (predictedDataPoints.isNotEmpty()) rememberLineCartesianLayer(
        rangeProvider = rangeProvider,
        lineProvider = LineCartesianLayer.LineProvider.series(
            predictedLine
        )
    ) else null
    val verticalRulerLayer = rememberLineCartesianLayer(
        rangeProvider = rangeProvider,
        lineProvider = LineCartesianLayer.LineProvider.series(
            rulerLine
        )
    )
    val horizontalRulerLayer = rememberLineCartesianLayer(
        rangeProvider = rangeProvider,
        lineProvider = LineCartesianLayer.LineProvider.series(
            rulerLine
        )
    )

    // Build the chart with the line layer and axes, and host it in a Compose layout
    CartesianChartHost(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .height(LocalConfiguration.current.screenHeightDp.dp / 4),
        chart = rememberCartesianChart(
            *(listOfNotNull(
                realLineLayer, predictedlineLayer, verticalRulerLayer, horizontalRulerLayer
            ).toTypedArray()),
            startAxis = startAxis,
            bottomAxis = bottomAxis,
        ),
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(scrollEnabled = false)
    )
}

/**
 * A helper function to calculate linear interpolation between two points.
 *
 * @param firstPoint The first point (X, Y) for interpolation.
 * @param secondPoint The second point (X, Y) for interpolation.
 * @param inputPoint The input point (X) for interpolation.
 * @return The interpolated Y value based on the input point:
 * - If `x == x1`, returns `y1`.
 * - If `x == x2`, returns `y2`.
 * - If `x1 == x21, returns the average of `y1` and `y2`.
 * - Else, returns a value according to the slope formula:
 *   `(y2 - y1) / (x2 - x1) * (x - x1) + y1`
 * - If the value somehow ends up non-finite, falls back to 0.
 * @author Vad1mChK
 */
private fun <X: Number, Y: Number> calculateLinearInterpolation(
        firstPoint: Pair<X, Y>,
        secondPoint: Pair<X, Y>,
        inputPoint: X
): Float {
    val x1 = firstPoint.first.toFloat().also {
        Log.d("INTERPOLATION", "First X: $it (${it.toRawBits().toString(16)})")
    }
    val y1 = firstPoint.second.toFloat().also {
        Log.d("INTERPOLATION", "First Y: $it (${it.toRawBits().toString(16)})")
    }
    val x2 = secondPoint.first.toFloat().also {
        Log.d("INTERPOLATION", "Second X: $it (${it.toRawBits().toString(16)})")
    }
    val y2 = secondPoint.second.toFloat().also {
        Log.d("INTERPOLATION", "Second Y: $it (${it.toRawBits().toString(16)})")
    }
    val x = inputPoint.toFloat().also {
        Log.d("INTERPOLATION", "Input X: $it (${it.toRawBits().toString(16)})")
    }

    return when {
        x1 == x2 -> (y1 + y2) / 2  // Handle duplicate X values
        x == x1 -> y1
        x == x2 -> y2
        else -> {
            val slope = (y2 - y1) / (x2 - x1)
            if (slope.isFinite()) slope * (x - x1) + y1 else Float.NaN
        }
    }.takeIf { it.isFinite() } ?: 0f  // Fallback to 0 if NaN
}