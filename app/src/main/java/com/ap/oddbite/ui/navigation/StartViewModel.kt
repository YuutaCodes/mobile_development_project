package com.ap.oddbite.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ap.oddbite.data.model.Role
import com.ap.oddbite.data.remote.AuthRemoteDataSource
import com.ap.oddbite.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface StartUiState {
    data object Loading : StartUiState
    data class Ready(val route: String) : StartUiState
}

class StartViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<StartUiState>(StartUiState.Loading)
    val uiState: StateFlow<StartUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = StartUiState.Ready(resolveRoute())
        }
    }

    private suspend fun resolveRoute(): String {
        return try {
            authRepository.getCurrentUser()?.let { Routes.homeFor(it.role)} ?: Routes.LOGIN
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Routes.LOGIN
        }

    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val dataSource =
                    AuthRemoteDataSource(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
                StartViewModel(AuthRepository(dataSource))
            }
        }
    }
}
