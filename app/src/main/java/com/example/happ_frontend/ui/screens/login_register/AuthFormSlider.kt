package com.example.happ_frontend.ui.screens.login_register

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthFormSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    labelText: String,
    min: Float = 0f,
    max: Float = 100f,
    steps: Int = 0,
    modifier: Modifier = Modifier
) {
    var textValue by remember { mutableStateOf(value.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val numberFormat = LocalContext.current.numberFormat

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Text input field
            TextField(
                value = textValue,
                onValueChange = {
                    textValue = it
                    val parsed = it.toFloatOrNull()
                    when {
                        parsed == null -> errorMessage = "Invalid number"
                        parsed < min -> errorMessage = "Minimum $min"
                        parsed > max -> errorMessage = "Maximum $max"
                        else -> {
                            errorMessage = null
                            onValueChange(parsed)
                        }
                    }
                },
                label = { Text(labelText) },
                modifier = Modifier.width(100.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                isError = errorMessage != null,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                )
            )

            // Slider
            Slider(
                value = value,
                onValueChange = {
                    onValueChange(it)
                    textValue = numberFormat.format(it)
                },
                valueRange = min..max,
                steps = steps,
                modifier = Modifier.weight(1f)
            )
        }

        // Error message
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

// Number format helper
val Context.numberFormat: NumberFormat
    get() = NumberFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 1
    }