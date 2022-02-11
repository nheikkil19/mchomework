package com.example.mchomework

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mchomework.home.Home
import com.example.mchomework.login.Login
import com.example.mchomework.reminder.Reminder

@Composable
fun ReminderApp(
    sharedPref: SharedPreferences,
    appState: ReminderAppState = rememberReminderAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = "home"
    ) {
        composable(route = "login") {
            Login(sharedPref, navController = appState.navController)
        }
        composable(route = "home") {
            Home(navController = appState.navController)
        }
        composable(route = "addReminder") {
            Reminder(navController = appState.navController, edit = false)
        }
        composable(route = "editReminder") {
            Reminder(navController = appState.navController, edit = true)
        }
    }
}