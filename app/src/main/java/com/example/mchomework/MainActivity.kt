package com.example.mchomework

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavType
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mchomework.home.Home
import com.example.mchomework.map.MyMap
import com.example.mchomework.profile.Profile
import com.example.mchomework.reminder.Reminder
import com.example.mchomework.ui.theme.MchomeworkTheme
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity() : ComponentActivity() {

    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(
            this,
            TextToSpeech.OnInitListener { status ->
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeechEngine.language = Locale.UK
                }
            })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE)
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            MchomeworkTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ReminderApp(sharedPref, fusedLocationClient, textToSpeechEngine, this)
                }
            }
        }
    }
}

@Composable
fun ReminderApp(
    sharedPref: SharedPreferences,
    fusedLocationClient: FusedLocationProviderClient,
    tts: TextToSpeech,
    activity: Activity
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
                location = position,
                tts = tts
            )
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
                fusedLocationClient = fusedLocationClient,
            )
        }
        composable(route = "profile") {
            Profile(
                navController = appState.navController,
                activity = activity
            )
        }
        activity("login") {
            activityClass = LoginActivity::class
        }
    }
}
