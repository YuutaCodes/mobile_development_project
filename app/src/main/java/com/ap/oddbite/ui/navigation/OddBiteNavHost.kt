package com.ap.oddbite.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ap.oddbite.R
import com.ap.oddbite.data.model.Role
import com.ap.oddbite.ui.auth.LoginRoute
import com.ap.oddbite.ui.auth.RegisterRoute

@Composable
fun OddBiteNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.START,
        modifier = modifier
    ) {
        composable(Routes.START) {
            val viewModel: StartViewModel = viewModel(factory = StartViewModel.Factory)
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(state) {
                (state as? StartUiState.Ready)?.let { ready ->
                    navController.navigate(ready.route) {
                        popUpTo(Routes.START) { inclusive = true }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        composable(Routes.LOGIN) {
            LoginRoute(
                onLoggedIn = { role ->
                        val home = when (role) {
                            Role.CUSTOMER -> Routes.CUSTOMER_HOME
                            Role.MERCHANT -> Routes.MERCHANT_HOME
                        }
                        navController.navigate(home) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onCreateAccountClick = { navController.navigate(Routes.REGISTER) },
                onForgotPasswordClick = {}
            )
        }
        composable(Routes.REGISTER) {
            RegisterRoute(
                onBackClick = { navController.popBackStack() },
                onRegistered = { role ->
                    navController.navigate(Routes.homeFor(role)) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.CUSTOMER_HOME) { PlaceholderScreen(R.string.customer_home_placeholder) }
        composable(Routes.MERCHANT_HOME) { PlaceholderScreen(R.string.merchant_home_placeholder) }
    }
}

@Composable
private fun PlaceholderScreen(@StringRes text: Int) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(text))
    }
}
