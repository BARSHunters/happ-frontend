package com.example.happ_frontend.ui.screens.login_register

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.domain.login_register.AuthState
import com.example.happ_frontend.ui.domain.login_register.AuthValidator
import com.example.happ_frontend.ui.domain.login_register.AuthViewModel
import com.example.happ_frontend.ui.navigation.HomeDest
import com.example.happ_frontend.ui.navigation.RegisterDest
import com.example.happ_frontend.ui.theme.Typography
import kotlinx.coroutines.launch

/**
 * A composable function that represents the login screen of the application.
 *
 * It provides a login form with the following elements:
 * -
 *
 * @param onLoginClick A lambda function that is invoked when the login button is clicked.
 * It receives the entered username and password as parameters.
 * @param onForgotPasswordClick A lambda function that is invoked when the "forgot password"
 * text is clicked. It is optional and defaults to an empty function.
 * @param onSwitchToRegisterClick A lambda function that is invoked when the "don't have an account"
 * text is clicked. Defaults to navigating to register
 *
 * @author Vad1mChK
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    navigationController: NavHostController? = null,
    viewModel: AuthViewModel = viewModel(),
    onForgotPasswordClick: () -> Unit = {},
    onSwitchToRegisterClick: (NavHostController) -> Unit = ::onSwitchToRegisterClickDefault,
    onLoginClick: () -> Unit = {
        viewModel.loginUser()
    }
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope() // Remember coroutine scope

    val uiState by viewModel.uiState.collectAsState()
    val authState by viewModel.authState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> @Composable {
                navigationController?.navigate(HomeDest.route)
            }
            is AuthState.Error -> @Composable {
                Toast.makeText(
                    context,
                    "Error: ${(authState as AuthState.Error).message}", Toast.LENGTH_LONG
                ).show()
            }
            AuthState.Loading -> {}
            AuthState.Idle -> {}
        }
    }

    val dontHaveAccountAnnotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            // Regular text part
            append(stringResource(R.string.auth_hint_dont_have_account))

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
                append(stringResource(R.string.auth_hint_dont_have_account_register))
            }
            pop()
        }
    }

    val registerNewAccountAnnotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            // Clickable part with annotation
            pushStringAnnotation(
                tag = "CLICKABLE_TAG",
                annotation = "forgot_password_action"
            )
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(stringResource(R.string.auth_hint_forgot_password_register))
            }
            pop()
        }
    }

    HorizontalPager(state = pagerState, userScrollEnabled = false) { pageIndex ->
        AuthFormPagerPage(pageIndex) {
            when (pageIndex) {
                0 -> Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(
                        16.dp,
                        alignment = Alignment.CenterVertically
                    )
                ) {
                    Row {
                        Text(
                            text = stringResource(R.string.auth_title_login),
                            fontWeight = FontWeight.Black,
                            fontSize = TextUnit(24f, TextUnitType.Sp),
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                        Image(
                            painter = painterResource(R.drawable.auth_cat1),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Bottom)
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
                        censorable = true,
                        censored = uiState.passwordCensored,
                        onCensoredChange = { viewModel.passwordCensored = it },
                        leadingIconVector = Icons.Default.Lock,
                        validator = AuthValidator.PasswordValidator()
                    )

                    ClickableText(
                        text = buildAnnotatedString {
                            pushStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                            append(stringResource(R.string.auth_hint_forgot_password))
                            pop()
                            toAnnotatedString()
                        },
                        onClick = {
                            onForgotPasswordClick()
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page = 1)
                            }
                        },
                        modifier = Modifier.align(Alignment.End),
                        style = Typography.bodyLarge
                    )

                    AuthFormButton(
                        text = stringResource(R.string.auth_button_login),
                        onClick = { onLoginClick() },
//                        enabledCondition = { viewModel.validateLogin() }
                        enabled = viewModel.validateLogin() && authState !is AuthState.Loading,
                        loading = authState is AuthState.Loading,
                    )

                    ClickableText(
                        text = dontHaveAccountAnnotatedString,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = { offset ->
                            dontHaveAccountAnnotatedString.getStringAnnotations(
                                tag = "CLICKABLE_TAG",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let {
                                Log.d("LoginScreen", "switching to register...")
                                navigationController?.let(onSwitchToRegisterClick)
                            }
                        },
                        style = Typography.bodyLarge
                    )
                }

                1 -> Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(
                        16.dp,
                        alignment = Alignment.CenterVertically
                    )
                ) {
                    Text(
                        text = stringResource(R.string.auth_title_forgot_password),
                        fontWeight = FontWeight.Black,
                        fontSize = TextUnit(24f, TextUnitType.Sp),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(R.string.auth_hint_forgot_password_condolence),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                    ClickableText(
                        text = registerNewAccountAnnotatedString,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = { offset ->
                            registerNewAccountAnnotatedString.getStringAnnotations(
                                tag = "CLICKABLE_TAG",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let {
                                Log.d("LoginScreen", "switching to login...")
                                navigationController?.let(onSwitchToRegisterClick)
                            }
                        },
                        style = Typography.bodyLarge
                    )
                }
            }
        }
    }
}

private fun onSwitchToRegisterClickDefault(navigationController: NavHostController) {
    navigationController.navigate(RegisterDest.route)
}