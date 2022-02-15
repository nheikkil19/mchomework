package com.example.mchomework

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mchomework.home.Home
import com.example.mchomework.login.Login
import com.example.mchomework.reminder.Reminder
import com.example.mchomework.ui.theme.MchomeworkTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

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

