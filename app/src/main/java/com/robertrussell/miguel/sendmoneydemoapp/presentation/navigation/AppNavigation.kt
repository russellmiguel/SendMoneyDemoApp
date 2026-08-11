package com.robertrussell.miguel.sendmoneydemoapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.robertrussell.miguel.sendmoneydemoapp.presentation.home.HomeScreen
import com.robertrussell.miguel.sendmoneydemoapp.presentation.login.LoginScreen
import com.robertrussell.miguel.sendmoneydemoapp.presentation.signup.SignUpScreen

sealed class Screen(val route: String) {
    object SignUp : Screen("signup")
    object Login : Screen("login")
    object Home : Screen("home/{userName}/{userEmail}") {
        fun createRoute(userName: String, userEmail: String) = "home/$userName/$userEmail"
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SignUp.route,
        modifier = modifier
    ) {
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onNavigateToHome = { user ->
                    navController.navigate(Screen.Home.createRoute(user.name, user.email)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.Home.route,
            arguments = listOf(
                navArgument("userName") { type = NavType.StringType },
                navArgument("userEmail") { type = NavType.StringType }
            )
        ) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
