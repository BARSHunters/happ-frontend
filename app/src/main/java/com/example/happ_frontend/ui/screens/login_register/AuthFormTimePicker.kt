package com.example.happ_frontend.ui.screens.login_register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.domain.weight.now
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthFormTimePicker(
    value: LocalTime?,
    onValueChange: (LocalTime?) -> Unit,
    labelText: String,
    leadingIconVector: ImageVector? = null,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showTimePicker by remember { mutableStateOf(false) }

    val formatter = remember {
        DateTimeFormatter.ofPattern("HH:mm")
    }

    val currentTime = remember { LocalTime.now() }
    val timePickerState = rememberTimePickerState(
        initialHour = value?.hour ?: currentTime.hour,
        initialMinute = value?.minute ?: currentTime.minute,
        is24Hour = true
    )

    TextField(
        value = value?.format(formatter) ?: "",
        onValueChange = {},
        label = { Text(labelText, color = MaterialTheme.colorScheme.primary) },
        leadingIcon = leadingIconVector?.let { icon ->
            { Icon(imageVector = icon, contentDescription = null) }
        },
        trailingIcon = {
            IconButton(onClick = { showTimePicker = true }) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null
                )
            }
        },
        modifier = modifier.fillMaxWidth(),
        readOnly = true,
        isError = errorMessage != null,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(),
        supportingText = {
            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        )
    )

    if (showTimePicker) {
        Dialog(
            onDismissRequest = { showTimePicker = false }
        ) {
            Card(
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    TimePicker(state = timePickerState)
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = { showTimePicker = false }
                        ) {
                            Text(stringResource(R.string.dialog_misc_buttons_cancel))
                        }
                        TextButton(
                            onClick = {
                                onValueChange(
                                    LocalTime.of(
                                        timePickerState.hour,
                                        timePickerState.minute
                                    )
                                )
                                showTimePicker = false
                            }
                        ) {
                            Text(stringResource(R.string.dialog_misc_buttons_ok))
                        }
                    }
                }
            }
        }
    }
}