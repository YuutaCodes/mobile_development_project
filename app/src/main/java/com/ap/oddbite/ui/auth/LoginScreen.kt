package com.ap.oddbite.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ap.oddbite.R
import com.ap.oddbite.data.model.Role
import com.ap.oddbite.ui.components.OddBiteButton
import com.ap.oddbite.ui.components.OddBiteButtonStyle
import com.ap.oddbite.ui.components.OddBiteTextField
import com.ap.oddbite.ui.theme.Inter
import com.ap.oddbite.ui.theme.OddBiteTheme

@Composable
fun LoginRoute(
    onLoggedIn: (Role) -> Unit,
    onCreateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.loggedInRole) {
        state.loggedInRole?.let(onLoggedIn)
    }

    LoginScreen(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::onLoginClick,
        onCreateAccountClick = onCreateAccountClick,
        onForgotPasswordClick = onForgotPasswordClick
    )
}

object Variables {
    // TODO: change to theme
    val textHeadline: Color = Color(0xFF161513)

    val textSubtitle: Color = Color(0xFF6B6860)

    val bgInverse: Color = Color(0xFF161513)
    val radiusLg: Dp = 16.dp
    val space20: Dp = 20.dp


    val space8: Dp = 8.dp
    val space12: Dp = 12.dp
    val space24: Dp = 24.dp
    val space32: Dp = 32.dp
    val space16: Dp = 16.dp

}

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Variables.space16),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Variables.space24, vertical = Variables.space32)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Variables.space8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.bite),
                contentDescription = null,
                modifier = Modifier.size(26.dp)
            )

            Text(
                text = stringResource(R.string.app_name),
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 26.sp,
                    lineHeight = 30.sp,
                    letterSpacing = (-0.78).sp,
                    color = Variables.textHeadline
                )
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(Variables.space12),
            modifier = Modifier.padding(top = Variables.space24)
        ) {
            Text(
                text = stringResource(R.string.catchphrase),
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    lineHeight = 38.sp,
                    letterSpacing = (-0.64).sp,
                    color = Variables.textHeadline
                )
            )

            Text(
                text = stringResource(R.string.motto),
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = Variables.textSubtitle
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(verticalArrangement = Arrangement.spacedBy(Variables.space16)) {
            OddBiteTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.login_email),
                placeholder = stringResource(R.string.login_email_placeholder),
                leadingIcon = R.drawable.ic_mail,
                keyboardType = KeyboardType.Email
            )

            OddBiteTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.login_password),
                leadingIcon = R.drawable.ic_lock,
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                TextButton(onClick = onForgotPasswordClick, contentPadding = PaddingValues(0.dp)) {
                    Text(
                        text = stringResource(R.string.login_forgot_password),
                        style = TextStyle(
                            fontFamily = Inter,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = Variables.textHeadline
                        )
                    )
                }
            }

            state.errorMessage?.let {
                Text(text = stringResource(it), color = MaterialTheme.colorScheme.error)
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(Variables.space12),
            modifier = Modifier.padding(top = Variables.space8)
        ) {
            OddBiteButton(
                text = stringResource(R.string.login_button),
                onClick = onLoginClick,
                isLoading = state.isLoading
            )

            OddBiteButton(
                text = stringResource(R.string.login_create_account),
                onClick = onCreateAccountClick,
                style = OddBiteButtonStyle.Secondary,
                enabled = !state.isLoading,
                trailingIcon = R.drawable.ic_arrow_forward
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800, widthDp = 390)
@Composable
private fun LoginScreenPreview() {
    OddBiteTheme {
        LoginScreen(
            state = LoginUiState(email = "test@example.com"),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onCreateAccountClick = {},
            onForgotPasswordClick = {}
        )
    }
}
