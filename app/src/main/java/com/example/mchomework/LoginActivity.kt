package com.example.mchomework

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
            putString("username", "") //user
            putString("password", "") //pass
            apply()
        }

        setContent {
            val appState = rememberReminderAppState()
            MchomeworkTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

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
            }
        }
    }
}