package com.ap.oddbite.ui.auth

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ap.oddbite.R
import com.ap.oddbite.data.model.Role
import com.ap.oddbite.data.remote.AuthRemoteDataSource
import com.ap.oddbite.data.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, errorMessage = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onRoleChange(role: Role) {
        _uiState.update { it.copy(role = role, errorMessage = null) }
    }

    fun onRegisterClick() {
        val state = _uiState.value
        if (state.isLoading) return

        val validationError = validate(state)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                authRepository.register(state.name.trim(), state.email.trim(), state.password, state.role)
                _uiState.update { it.copy(isLoading = false, registeredRole = state.role) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = errorFor(e)) }
            }
        }
    }

    @StringRes
    private fun validate(state: RegisterUiState): Int? = when {
        state.name.isBlank() || state.email.isBlank() || state.password.isBlank() ->
            R.string.register_error_empty_fields

        !Patterns.EMAIL_ADDRESS.matcher(state.email.trim()).matches() ->
            R.string.register_error_invalid_email

        state.password.length < MIN_PASSWORD_LENGTH -> R.string.register_error_weak_password
        else -> null
    }

    private fun errorFor(e: Exception): Int = when (e) {
        is FirebaseAuthUserCollisionException -> R.string.register_error_email_in_use
        is FirebaseAuthWeakPasswordException -> R.string.register_error_weak_password
        is FirebaseAuthInvalidCredentialsException -> R.string.register_error_invalid_email
        is FirebaseNetworkException -> R.string.register_error_no_internet
        else -> R.string.register_error_unknown
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6

        val Factory = viewModelFactory {
            initializer {
                val dataSource =
                    AuthRemoteDataSource(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
                RegisterViewModel(AuthRepository(dataSource))
            }
        }
    }
}
