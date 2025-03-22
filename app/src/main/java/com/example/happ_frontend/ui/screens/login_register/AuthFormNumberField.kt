import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.happ_frontend.ui.domain.login_register.LoginRegisterValidationResult
import com.example.happ_frontend.ui.domain.login_register.LoginRegisterValidator
import com.example.happ_frontend.ui.domain.login_register.format
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthFormNumberField(
    value: Float,
    onValueChange: (Float) -> Unit,
    min: Float? = null,
    max: Float? = null,
    precision: Int = 3,
    labelText: String
) {
    val focusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf(value.format(precision)) }
    var isFocused by remember { mutableStateOf(false) }
    var validationResult by remember {
        mutableStateOf<LoginRegisterValidationResult>(
            LoginRegisterValidationResult.Success
        )
    }

    val validator = remember(min, max, precision) {
        LoginRegisterValidator.NumberInputValidator(min, max, precision)
    }

    // Synchronize with external value changes when not focused
    LaunchedEffect(value) {
        if (!isFocused) {
            text = value.format(precision)
            validationResult = validator.validate(text)
        }
    }

    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            validationResult = validator.validate(newText)
            if (newText.toBigDecimalOrNull() != null) {
                onValueChange(newText.toBigDecimal().toFloat())
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (!focusState.isFocused) {
                    when (val result = validator.validate(text)) {
                        is LoginRegisterValidationResult.Success -> {
                            text = value.format(precision)
                        }

                        is LoginRegisterValidationResult.Failure -> {
                            validationResult = result
                        }
                    }
                }
            }
            .background(color = MaterialTheme.colorScheme.background),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusRequester.freeFocus() }
        ),
        singleLine = true,
        isError = validationResult is LoginRegisterValidationResult.Failure,
        label = { Text(labelText) },
        supportingText = {
            (validationResult as? LoginRegisterValidationResult.Failure)?.let { error ->
                Text(text = stringResource(error.errorResId, *error.formatArgs))
            }
        }
    )
}