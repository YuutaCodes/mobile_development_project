package com.ap.oddbite.ui.auth

import androidx.annotation.StringRes
import com.ap.oddbite.data.model.Role

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    @param:StringRes val errorMessage: Int? = null,
    val loggedInRole: Role? = null
)
