package com.example.happ_frontend.ui.screens.login_register

import AuthFormDatePicker
import AuthFormNumberField
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.domain.login_register.AuthFormData
import com.example.happ_frontend.ui.domain.login_register.AuthValidator
import com.example.happ_frontend.ui.domain.login_register.AuthViewModel
import com.example.happ_frontend.ui.domain.login_register.getNameMap
import com.example.happ_frontend.ui.navigation.LoginDest
import com.example.happ_frontend.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen(
    navigationController: NavHostController? = null,
    onCheckIfAccountExistsClick: () -> Unit = { },
    onRegisterClick: () -> Unit = {},
    onSwitchToLoginClick: (NavHostController) -> Unit = ::onSwitchToLoginClickDefault,
    viewModel: AuthViewModel = viewModel()
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope() // Remember coroutine scope

    val uiState by viewModel.uiState.collectAsState()

    val alreadyHaveAccountAnnotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            // Regular text part
            append(stringResource(R.string.auth_hint_already_have_account))

            // Add space between texts if needed
            append(" ")

            // Clickable part with annotation
            pushStringAnnotation(
                tag = "CLICKABLE_TAG",
                annotation = "register_action"
            )
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(stringResource(R.string.auth_hint_already_have_account_login))
            }
            pop()
        }
    }

    HorizontalPager(state = pagerState, userScrollEnabled = false) { pageIndex ->
        AuthFormPagerPage(pageIndex) {
            when (pageIndex) {
                0 -> { // Initial registration data
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.auth_title_register),
                            fontWeight = FontWeight.Black,
                            fontSize = TextUnit(24f, TextUnitType.Sp),
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.auth_cat2),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth(fraction = 0.6f)
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    AuthFormTextField(
                        value = uiState.username,
                        onValueChange = { viewModel.username = it },
                        labelText = stringResource(R.string.auth_field_username),
                        leadingIconVector = Icons.Default.AccountCircle,
                        validator = AuthValidator.UsernameValidator()
                    )
                    AuthFormTextField(
                        value = uiState.password,
                        onValueChange = { viewModel.password = it },
                        labelText = stringResource(R.string.auth_field_password),
                        leadingIconVector = Icons.Default.Lock,
                        censorable = true,
                        censored = uiState.passwordCensored,
                        onCensoredChange = { viewModel.passwordCensored = it },
                        validator = AuthValidator.PasswordValidator()
                    )
                    AuthFormTextField(
                        value = uiState.confirmPassword,
                        onValueChange = { viewModel.confirmPassword = it },
                        labelText = stringResource(R.string.auth_field_password_confirm),
                        leadingIconVector = Icons.Default.Lock,
                        censorable = true,
                        censored = uiState.passwordCensored,
                        onCensoredChange = { viewModel.passwordCensored = it },
                        validator = AuthValidator.PasswordMatchValidator(uiState.password)
                    )
                    AuthFormButton(
                        text = stringResource(R.string.auth_button_register_continue),
                        // enabledCondition = { viewModel.validateRegisterFirstPart() },
                        enabled = viewModel.validateRegisterFirstPart(),
                        onClick = {
                            Log.d("RegisterScreen", "continue button clicked")
                            // TODO check if username doesn't already exist, then allow scroll, else display error
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                    )
                    Text(
                        text = viewModel.validateRegisterFirstPart().toString()
                    )
                }

                1 -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.auth_title_register),
                            fontWeight = FontWeight.Black,
                            fontSize = TextUnit(24f, TextUnitType.Sp),
                            modifier = Modifier.align(Alignment.CenterVertically)

                        )
                        Image(
                            painter = painterResource(R.drawable.auth_cat0),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .fillMaxWidth()
                        )
                    }
                    AuthFormTextField(
                        value = uiState.name,
                        onValueChange = { viewModel.name = it },
                        labelText = stringResource(R.string.auth_field_name),
                        leadingIconVector = Icons.Default.Face,
                        validator = AuthValidator.NameValidator()
                    )
                    AuthFormDatePicker(
                        value = uiState.birthDate,
                        onValueChange = { it?.let { newDate -> viewModel.birthDate = newDate } },
                        labelText = stringResource(R.string.auth_field_birth_date),
                        // leadingIconVector = Icons.Default.DateRange
                    )
                    AuthFormEnumChipSelect(
                        value = uiState.gender,
                        onValueChange = { viewModel.gender = it },
                        labelText = stringResource(R.string.auth_field_gender),
                        nameMap = getNameMap(
                            context = LocalContext.current,
                            prefix = "biom.gender",
                            separator = "."
                        )
                    )
                    AuthFormNumberField(
                        value = uiState.heightCm.toFloat(),
                        onValueChange = { viewModel.heightCm = it.toInt() },
                        min = AuthFormData.MIN_HEIGHT_CM.toFloat(),
                        max = AuthFormData.MAX_HEIGHT_CM.toFloat(),
                        labelText = stringResource(R.string.auth_field_height),
                    )
                    AuthFormNumberField(
                        value = uiState.weightKg,
                        onValueChange = { viewModel.weightKg = it },
                        min = AuthFormData.MIN_WEIGHT_KG,
                        max = AuthFormData.MAX_WEIGHT_KG,
                        labelText = stringResource(R.string.auth_field_weight),
                        precision = 3
                    )
                    AuthFormEnumChipSelect(
                        value = uiState.weightDesire,
                        onValueChange = { viewModel.weightDesire = it },
                        labelText = stringResource(R.string.auth_field_weight_desire),
                        nameMap = getNameMap(
                            context = LocalContext.current,
                            prefix = "biom.weight_desire",
                            separator = "."
                        )
                    )
                    AuthFormButton(
                        text = stringResource(R.string.auth_button_register),
                        // enabledCondition = { viewModel.validateRegister() },
                        enabled = viewModel.validateRegister(),
                        onClick = {
                            Log.d("RegisterScreen", "register button clicked")
                        },
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ClickableText(
                    text = alreadyHaveAccountAnnotatedString,
                    onClick = { offset ->
                        alreadyHaveAccountAnnotatedString.getStringAnnotations(
                            tag = "CLICKABLE_TAG",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let {
                            Log.d("RegisterScreen", "switching to login...")
                            navigationController?.let(onSwitchToLoginClick)
                        }
                    },
                    style = Typography.bodyLarge
                )
            }
        }
    }
}

private fun onSwitchToLoginClickDefault(navigationController: NavHostController) {
    navigationController.navigate(LoginDest.route)
}