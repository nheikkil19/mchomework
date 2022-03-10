package com.example.mchomework.reminder

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mchomework.Graph
import com.example.mchomework.data.entity.Reminder
import com.example.mchomework.data.repository.ReminderRepository
import com.example.mchomework.notification.createNotificationChannel
import com.example.mchomework.notification.deleteNotification
import com.example.mchomework.notification.setNotificationAtTime
import com.example.mchomework.notification.setRepeatingNotificationAtTime
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Thread.sleep
import java.util.*

class ReminderViewModel(
    val fusedLocationClient: FusedLocationProviderClient,
    private val reminderRepository: ReminderRepository = Graph.reminderRepository
): ViewModel() {
    private val _state = MutableStateFlow(ReminderViewState())

    val state: StateFlow<ReminderViewState>
        get() = _state

    suspend fun saveReminder(reminder: Reminder): Long {
        val ret = reminderRepository.addReminder(reminder)

        if (reminder.notify) {
            when {
                reminder.daily -> {
                    setRepeatingNotificationAtTime(
                        delay = reminder.reminder_time - Date().time,
                        message = reminder.message,
                        id = reminder.id,
                        repeatPeriod = 1
                    )
                }
                reminder.weekly -> {
                    setRepeatingNotificationAtTime(
                        delay = reminder.reminder_time - Date().time,
                        message = reminder.message,
                        id = reminder.id,
                        repeatPeriod = 7
                    )
                }
                else -> {
                    setNotificationAtTime(
                        delay = reminder.reminder_time - Date().time,
                        message = reminder.message,
                        id = reminder.id
                    )
                }
            }
        }
        return ret
    }

    suspend fun getReminder(id: Int): Reminder? {
        return reminderRepository.getReminder(id)
    }

    suspend fun updateReminder(reminder: Reminder) {
        reminderRepository.updateReminder(reminder)
        if (reminder.notify) {
            when {
                reminder.daily -> {
                    setRepeatingNotificationAtTime(
                        delay = reminder.reminder_time - Date().time,
                        message = reminder.message,
                        id = reminder.id,
                        repeatPeriod = 1
                    )
                }
                reminder.weekly -> {
                    setRepeatingNotificationAtTime(
                        delay = reminder.reminder_time - Date().time,
                        message = reminder.message,
                        id = reminder.id,
                        repeatPeriod = 7
                    )
                }
                else -> {
                    setNotificationAtTime(
                        delay = reminder.reminder_time - Date().time,
                        message = reminder.message,
                        id = reminder.id
                    )
                }
            }
        }
        else {
            deleteNotification(reminder.id)
        }
    }

    suspend fun deleteReminder(reminder: Reminder) {
        deleteNotification(reminder.id)
        return reminderRepository.deleteReminder(reminder)
    }

    fun hide() {
        getUnhidden()
    }

    private fun getUnhidden() {
        var myLocation: Location?
        if (ActivityCompat.checkSelfPermission(
                Graph.appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                Graph.appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                myLocation = location
                Log.d("tag", "hello2 $myLocation")
                if (state.value.hidden) {
                    viewModelScope.launch {
                        reminderRepository.getRemindersBefore(
                            myLocation?.longitude ?: 0.0,
                            myLocation?.latitude ?: 0.0
                        ).collect { list ->
                            _state.value = ReminderViewState(
                                reminders = list,
                                hidden = false
                            )
                        }
                    }
                } else {
                    viewModelScope.launch {
                        reminderRepository.getReminders().collect { list ->
                            _state.value = ReminderViewState(
                                reminders = list,
                                hidden = true
                            )
                        }
                    }
                }
            }
    }

    init {
        createNotificationChannel()
        getUnhidden()
    }
}

data class ReminderViewState(
    val reminders: List<Reminder> = emptyList(),
    val hidden: Boolean = true
)