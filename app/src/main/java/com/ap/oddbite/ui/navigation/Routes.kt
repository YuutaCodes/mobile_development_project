package com.ap.oddbite.ui.navigation

import com.ap.oddbite.data.model.Role

object Routes {
    const val START = "start"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CUSTOMER_HOME = "customer_home"
    const val MERCHANT_HOME = "merchant_home"

    fun homeFor(role: Role): String = when (role) {
        Role.CUSTOMER -> CUSTOMER_HOME
        Role.MERCHANT -> MERCHANT_HOME
    }
}
