package com.example.mchomework

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.Observer
import androidx.navigation.NavType
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mchomework.data.entity.Reminder
import com.example.mchomework.home.Home
import com.example.mchomework.reminder.Reminder
import com.example.mchomework.reminder.ReminderViewModel
import com.example.mchomework.ui.theme.MchomeworkTheme

class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE)
        setContent {
            MchomeworkTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ReminderApp(sharedPref)
                }
            }
        }
    }
}

@Composable
fun ReminderApp(
    sharedPref: SharedPreferences
) {
    val appState = rememberReminderAppState()
    val startDest = if (sharedPref.getBoolean("loggedIn", false)) "home"
    else "login"

    NavHost(
        navController = appState.navController,
        startDestination = startDest
    ) {

        composable(route = "home") {
            Home(navController = appState.navController, sharedPref = sharedPref)
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
