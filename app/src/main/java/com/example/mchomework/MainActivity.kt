package com.example.mchomework

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mchomework.home.Home
import com.example.mchomework.login.Login
import com.example.mchomework.ui.theme.MchomeworkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MchomeworkTheme {
                val sharedPref = getPreferences(Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("username", "student")
                    putString("password", "abc123")
                    apply()
                    }
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ReminderApp(sharedPref)
                }
            }
        }
    }
}

