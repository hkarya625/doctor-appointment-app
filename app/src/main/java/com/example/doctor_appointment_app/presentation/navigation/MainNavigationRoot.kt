package com.arya.bookmydoc.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arya.bookmydoc.domain.model.Doctor
import com.arya.bookmydoc.presentation.features.appointment.AppointmentScreen
import com.arya.bookmydoc.presentation.features.doctor_details.DoctorDetailsScreen
import com.arya.bookmydoc.presentation.features.home.HomeScreen
import com.arya.bookmydoc.presentation.features.home.NavigationBottomBar
import com.arya.bookmydoc.presentation.features.profile.ProfileScreen
import com.arya.bookmydoc.presentation.features.top_doctors.TopDoctorScreen
import com.arya.bookmydoc.presentation.utils.dialNumber
import com.arya.bookmydoc.presentation.utils.openBrowser
import com.arya.bookmydoc.presentation.utils.sendSms
import com.arya.bookmydoc.presentation.utils.shareText

@Composable
fun MainNavigationRoot(
    parentNavController: NavHostController
){
    val bottomNavController = rememberNavController()

    val bottomBarRoutes = listOf(
        "home",
        "appointment",
        "profile"
    )
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val shouldShowBottomBar = currentDestination
        ?.hierarchy
        ?.any { it.route != null && it.route in bottomBarRoutes } == true


    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = shouldShowBottomBar,
                enter = slideInVertically(
                    // slide from full height (it) up to 0
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 250)
                ) + fadeIn(animationSpec = tween(250)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 200)
                ) + fadeOut(animationSpec = tween(200))
            ) {
                NavigationBottomBar(navController = bottomNavController)
            }

        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Screen.Home.route) {
                HomeScreen(
                    onDoctorDetails = { doctorId ->
                        bottomNavController.navigate(Screen.Doctor.withId(doctorId))
                    },
                    onAllDoctor = {
                        bottomNavController.navigate(Screen.TopDoctors.route)
                    }
                )
            }
            composable(
                route = Screen.Doctor.route,
                arguments = listOf(
                    navArgument("doctorId"){type = NavType.StringType}
                )
            ) { backstackEntry->

                val doctorId = backstackEntry.arguments?.getString("doctorId")
                val context = LocalContext.current
                doctorId?.let {
                    DoctorDetailsScreen(
                        doctorId = it,
                        onBack = { bottomNavController.popBackStack() },
                        onWebsite = { uri -> openBrowser(context, uri) },
                        onSendSms = { mobile, body -> sendSms(context, mobile, body) },
                        onDial = { mobile -> dialNumber(context, mobile) },
                        onDirection = { location -> openBrowser(context, location) },
                        onShare = { subject, text -> shareText(context, subject, text) },

                        onGoHome = {
                            bottomNavController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        onViewAppointment = {
                            bottomNavController.navigate(Screen.Appointment.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

            composable(Screen.TopDoctors.route) {
                TopDoctorScreen(
                    onBack = { bottomNavController.popBackStack() },
                    onDoctorDetails = {
                        bottomNavController.navigate(Screen.Doctor.route)
                    }
                )
            }

            composable(Screen.Appointment.route) {
                AppointmentScreen()
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        parentNavController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Main.route) { inclusive = true }
                        }
                    },
                    onProfileClick = {

                    }
                )
            }
        }
    }
}