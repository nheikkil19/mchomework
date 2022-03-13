package com.example.mchomework

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mchomework.login.Login
import com.example.mchomework.ui.theme.MchomeworkTheme

class LoginActivity(): ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("username", "user") //user
            putString("password", "pass") //pass
            apply()
        }
        setContent {
            MchomeworkTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    loginApp(sharedPref = sharedPref)
                }
            }
        }
    }
}

@Composable
fun loginApp(
    sharedPref: SharedPreferences
) {
    val appState = rememberReminderAppState()
    NavHost(
        navController = appState.navController,
        startDestination = "login"
        ) {
        composable("login") {
            Login(sharedPref = sharedPref, navController = appState.navController)
        }
        activity(route = "home") {
            activityClass = MainActivity::class
        }
    }
}