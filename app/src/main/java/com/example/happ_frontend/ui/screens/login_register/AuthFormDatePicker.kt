import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
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
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.domain.login_register.fromEpochMilliseconds
import com.example.happ_frontend.ui.domain.login_register.toEpochMilliseconds
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthFormDatePicker(
    value: LocalDate?,
    onValueChange: (LocalDate?) -> Unit,
    labelText: String,
    leadingIconVector: ImageVector? = null,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember {
        LocalDate.Formats.ISO
    }

    // Convert between LocalDate and timestamp
    val initialDate = value?.toEpochMilliseconds()

    TextField(
        value = value?.format(dateFormatter) ?: "",
        onValueChange = {}, // Disable manual editing
        label = { Text(labelText, color = MaterialTheme.colorScheme.primary) },
        leadingIcon = leadingIconVector?.let { it ->
            {
                Icon(imageVector = it, contentDescription = null)
            }
        },
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = stringResource(R.string.dialog_datepicker_header)
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
        ),
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onValueChange(LocalDate.fromEpochMilliseconds(it))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(stringResource(R.string.dialog_misc_buttons_ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text(stringResource(R.string.dialog_misc_buttons_cancel))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
            )
        }
    }
}