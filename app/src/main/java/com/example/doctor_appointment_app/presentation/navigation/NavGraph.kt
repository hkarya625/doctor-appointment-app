package com.arya.bookmydoc.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arya.bookmydoc.domain.model.Doctor


import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arya.bookmydoc.presentation.features.intro.IntroScreen
import com.arya.bookmydoc.presentation.features.user.LoginScreen
import com.arya.bookmydoc.presentation.features.user.SignupScreen
import com.arya.bookmydoc.presentation.utils.dialNumber
import com.arya.bookmydoc.presentation.utils.openBrowser
import com.arya.bookmydoc.presentation.utils.sendSms
import com.arya.bookmydoc.presentation.utils.shareText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun AppNavigation(
    isUserLoggedIn: Boolean
) {
    val navController = rememberNavController()
    val startDestination = if (isUserLoggedIn) Screen.Main.route else Screen.Intro.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable(Screen.Intro.route) {
            IntroScreen {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Intro.route) { inclusive = true }
                }
            }
        }


        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onRegistrationSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.Main.route) {
            MainNavigationRoot(parentNavController = navController)
        }
    }
}
sealed class Screen(val route: String) {
    data object Intro : Screen("intro")
    data object Home : Screen("home")
    data object Doctor : Screen("doctor/{doctorId}"){
        fun withId(doctorId: String):String = "doctor/$doctorId"
    }
    data object TopDoctors : Screen("topDoctors")
    data object Appointment : Screen("appointment")
    data object Profile : Screen("profile")
    data object Login : Screen("login")
    data object Signup : Screen("signup")
    data object Main:Screen("main")
}
