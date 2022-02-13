package com.example.mchomework

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mchomework.data.entity.Reminder
import com.example.mchomework.home.Home
import com.example.mchomework.login.Login
import com.example.mchomework.reminder.Reminder
import com.google.android.gms.location.FusedLocationProviderClient

@Composable
fun ReminderApp(
    sharedPref: SharedPreferences,
    appState: ReminderAppState = rememberReminderAppState(),
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

    }
}