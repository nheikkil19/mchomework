package com.example.mchomework

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.navigation.NavType
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mchomework.home.Home
import com.example.mchomework.map.MyMap
import com.example.mchomework.reminder.Reminder
import com.example.mchomework.ui.theme.MchomeworkTheme
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE)
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            MchomeworkTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ReminderApp(sharedPref, fusedLocationClient)
                }
            }
        }
    }
}

@Composable
fun ReminderApp(
    sharedPref: SharedPreferences,
    fusedLocationClient: FusedLocationProviderClient
) {
    val appState = rememberReminderAppState()
    val startDest = if (sharedPref.getBoolean("loggedIn", false)) "home"
    else "login"
    var position: LatLng? = null

    NavHost(
        navController = appState.navController,
        startDestination = startDest
    ) {

        composable(route = "home") {
            Home(
                navController = appState.navController,
                sharedPref = sharedPref,
                fusedLocationClient = fusedLocationClient,
                location = position
            )
//            position = null
        }
        composable(route = "addReminder") {
            Reminder(
                navController = appState.navController,
                edit = false,
                markerPosition = position,
                fusedLocationClient = fusedLocationClient
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
                markerPosition = position,
                fusedLocationClient = fusedLocationClient
            )
        }
        composable(route = "map") {
            position = MyMap(
                navController = appState.navController,
                fusedLocationClient = fusedLocationClient
            )
        }
        activity("login") {
            activityClass = LoginActivity::class
        }
    }
}


class MyLocationCallback(): LocationCallback() {
    override fun onLocationAvailability(p0: LocationAvailability) {
        super.onLocationAvailability(p0)
        Log.d("tag", "availability")
    }

    override fun onLocationResult(p0: LocationResult) {
        super.onLocationResult(p0)
        Log.d("tag", "result")
    }
}