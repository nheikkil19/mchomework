package com.example.mchomework

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mchomework.home.Home
import com.example.mchomework.login.Login

@Composable
fun ReminderApp(
    sharedPref: SharedPreferences,
    appState: ReminderAppState = rememberReminderAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            Login(sharedPref, navController = appState.navController)
        }
        composable(route = "home") {
            Home(navController = appState.navController)
        }
    }
}