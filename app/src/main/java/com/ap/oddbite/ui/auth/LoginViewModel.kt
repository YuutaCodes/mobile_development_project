package com.ap.oddbite.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ap.oddbite.R
import com.ap.oddbite.data.remote.AuthRemoteDataSource
import com.ap.oddbite.data.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onLoginClick() {
        val state = _uiState.value
        if (state.isLoading) return
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = R.string.login_error_empty_fields) }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val user = authRepository.login(state.email.trim(), state.password)
                _uiState.update { it.copy(isLoading = false, loggedInRole = user.role) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = errorFor(e)) }
            }
        }
    }

    private fun errorFor(e: Exception): Int = when (e) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> R.string.login_error_invalid_credentials
        is FirebaseNetworkException -> R.string.login_error_no_internet
        is IllegalStateException -> R.string.login_error_profile_missing
        else -> R.string.login_error_unknown
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val dataSource =
                    AuthRemoteDataSource(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
                LoginViewModel(AuthRepository(dataSource))
            }
        }
    }
}
