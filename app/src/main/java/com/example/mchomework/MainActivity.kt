package com.example.mchomework

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.example.mchomework.map.MyMap
import com.example.mchomework.reminder.Reminder
import com.example.mchomework.reminder.ReminderViewModel
import com.example.mchomework.ui.theme.MchomeworkTheme
import com.google.android.gms.maps.model.LatLng

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
    var markerPosition: LatLng? = null

    NavHost(
        navController = appState.navController,
        startDestination = startDest
    ) {

        composable(route = "home") {
            Home(navController = appState.navController, sharedPref = sharedPref)
            markerPosition = null
        }
        composable(route = "addReminder") {
            Reminder(
                navController = appState.navController,
                edit = false,
                markerPosition = markerPosition
            )
        }
        composable(route = "editReminder{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) {
            Reminder(
                navController = appState.navController,
                edit = true,
                reminderId = it.arguments?.getInt("id"),
                markerPosition = markerPosition
            )
        }
        composable(route = "map") {
            markerPosition = MyMap(navController = appState.navController)
        }
        activity("login") {
            activityClass = LoginActivity::class
        }
    }
}
