package com.example.mchomework

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mchomework.home.Home
import com.example.mchomework.login.Login
import com.example.mchomework.reminder.Reminder

@Composable
fun ReminderApp(
    appState: ReminderAppState = rememberReminderAppState(),
) {
    NavHost(
        navController = appState.navController,
        startDestination = "home"
    ) {

        composable(route = "home") {
            Home(navController = appState.navController)
        }
        composable(route = "addReminder") {
            Reminder(navController = appState.navController, edit = false)
        }
        composable(route = "editReminder{id}",
            arguments = listOf(navArgument("id") {
               type = NavType.IntType
               defaultValue = 0
           })) {
           Reminder(navController = appState.navController, edit = true, reminderId = it.arguments?.getInt("id"))
        }
        activity("login") {
            activityClass = LoginActivity::class
        }
    }
}