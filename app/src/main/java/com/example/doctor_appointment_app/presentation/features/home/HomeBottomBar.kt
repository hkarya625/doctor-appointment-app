package com.arya.bookmydoc.presentation.features.home

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arya.bookmydoc.R
import com.arya.bookmydoc.presentation.navigation.BottomNavItem
import com.arya.bookmydoc.presentation.navigation.Screen

@Composable
fun NavigationBottomBar(
    navController: NavController
) {
    val navItems = listOf(
        BottomNavItem(
            title = "Home",
            route = Screen.Home.route,
            filledIcon = R.drawable.ic_home_filled,
            outlinedIcon = R.drawable.ic_home_outlined
        ),
        BottomNavItem(
            title = "Schedule",
            route = Screen.Appointment.route,
            filledIcon = R.drawable.ic_calendar_filled,
            outlinedIcon = R.drawable.ic_calendar_outlined
        ),
        BottomNavItem(
            title = "Profile",
            route = Screen.Profile.route,
            filledIcon = R.drawable.ic_profile_filled,
            outlinedIcon = R.drawable.ic_profile_outlined
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = colorResource(R.color.mint),
        tonalElevation = 1.dp,
        modifier = Modifier.height(60.dp),
        windowInsets = WindowInsets(0)
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
//                            popUpTo(navController.graph.startDestinationId) {
//                                saveState = true
//                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (selected) item.filledIcon else item.outlinedIcon
                        ),
                        contentDescription = item.title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(text = item.title, fontSize = 10.sp)
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(R.color.teal),
                    selectedTextColor = colorResource(R.color.teal),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBottomBarPreview(){

}