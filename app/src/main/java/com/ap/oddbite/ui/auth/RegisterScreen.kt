package com.ap.oddbite.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ap.oddbite.R
import com.ap.oddbite.data.model.Role
import com.ap.oddbite.ui.components.OddBiteButton
import com.ap.oddbite.ui.components.OddBiteRoleOption
import com.ap.oddbite.ui.components.OddBiteTextField
import com.ap.oddbite.ui.components.OddBiteTopBar
import com.ap.oddbite.ui.theme.Inter
import com.ap.oddbite.ui.theme.OddBiteTheme

@Composable
fun RegisterRoute(onBackClick: () -> Unit, onRegistered: (Role) -> Unit) {
    val viewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.registeredRole) {
        state.registeredRole?.let(onRegistered)
    }

    RegisterScreen(
        state = state,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRoleChange = viewModel::onRoleChange,
        onRegisterClick = viewModel::onRegisterClick,
        onBackClick = onBackClick
    )
}

@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRoleChange: (Role) -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(modifier = modifier.fillMaxSize()) {
        OddBiteTopBar(
            title = stringResource(R.string.login_create_account),
            onBackClick = onBackClick
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Variables.space16),
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = Variables.space24,
                    top = Variables.space8,
                    end = Variables.space24,
                    bottom = Variables.space32
                )
        ) {
            OddBiteTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = stringResource(R.string.register_name),
                placeholder = stringResource(R.string.register_name_placeholder),
                leadingIcon = R.drawable.ic_person,
                enabled = !state.isLoading
            )

            OddBiteTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.login_email),
                placeholder = stringResource(R.string.login_email_placeholder),
                leadingIcon = R.drawable.ic_mail,
                keyboardType = KeyboardType.Email,
                enabled = !state.isLoading
            )

            OddBiteTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.login_password),
                leadingIcon = R.drawable.ic_lock,
                keyboardType = KeyboardType.Password,
                isPassword = true,
                enabled = !state.isLoading
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(Variables.space8),
                modifier = Modifier.padding(top = Variables.space8)
            ) {
                Text(
                    text = stringResource(R.string.register_role_label).uppercase(),
                    style = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        letterSpacing = 0.66.sp
                    ),
                    color = colors.onSurfaceVariant
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Variables.space12),
                    modifier = Modifier.selectableGroup()
                ) {
                    OddBiteRoleOption(
                        title = stringResource(R.string.register_role_customer),
                        description = stringResource(R.string.register_role_customer_description),
                        icon = R.drawable.ic_person_role,
                        selected = state.role == Role.CUSTOMER,
                        onClick = { onRoleChange(Role.CUSTOMER) },
                        modifier = Modifier.weight(1f)
                    )
                    OddBiteRoleOption(
                        title = stringResource(R.string.register_role_merchant),
                        description = stringResource(R.string.register_role_merchant_description),
                        icon = R.drawable.ic_store,
                        selected = state.role == Role.MERCHANT,
                        onClick = { onRoleChange(Role.MERCHANT) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            state.errorMessage?.let {
                Text(text = stringResource(it), color = colors.error)
            }

            OddBiteButton(
                text = stringResource(R.string.login_create_account),
                onClick = onRegisterClick,
                isLoading = state.isLoading
            )

            Text(
                text = stringResource(R.string.register_terms),
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Center
                ),
                color = colors.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800, widthDp = 390)
@Composable
private fun RegisterScreenPreview() {
    OddBiteTheme(dynamicColor = false) {
        RegisterScreen(
            state = RegisterUiState(name = "Sam Peeters", email = "sam@example.com"),
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onRoleChange = {},
            onRegisterClick = {},
            onBackClick = {}
        )
    }
}
