package com.ap.oddbite.ui.auth

import androidx.annotation.StringRes
import com.ap.oddbite.data.model.Role

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val role: Role = Role.CUSTOMER,
    val isLoading: Boolean = false,
    @param:StringRes val nameError: Int? = null,
    @param:StringRes val emailError: Int? = null,
    @param:StringRes val passwordError: Int? = null,
    @param:StringRes val errorMessage: Int? = null,
    val registeredRole: Role? = null
)
