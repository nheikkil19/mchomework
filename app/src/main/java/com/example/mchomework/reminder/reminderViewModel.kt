package com.example.mchomework.reminder

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mchomework.Graph
import com.example.mchomework.data.entity.Reminder
import com.example.mchomework.data.repository.ReminderRepository
import com.example.mchomework.notification.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class ReminderViewModel(
    val fusedLocationClient: FusedLocationProviderClient,
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
    private val activity: Activity,
    location: LatLng?
): ViewModel() {
    private val _state = MutableStateFlow(ReminderViewState(location = location))

    val state: StateFlow<ReminderViewState>
        get() = _state

    suspend fun saveReminder(reminder: Reminder): Long {
        val ret = reminderRepository.addReminder(reminder)

        if (reminder.notify) {
            when {
                reminder.reminder_time == (-1).toLong() -> {
                    setNotificationAtPlace(
                        message = reminder.message,
                        id = ret.toInt()
                    )
                }
                reminder.daily -> {
                    setRepeatingNotificationAtTime(
                        delay = reminder.reminder_time - Date().time,
                        message = reminder.message,
                        id = ret.toInt(),
                        repeatPeriod = 1
                    )
                }
                reminder.weekly -> {
                    setRepeatingNotificationAtTime(
                        delay = reminder.reminder_time - Date().time,
                        message = reminder.message,
                        id = ret.toInt(),
                        repeatPeriod = 7
                    )
                }
                else -> {
                    setNotificationAtTime(
                        delay = reminder.reminder_time - Date().time,
                        message = reminder.message,
                        id = ret.toInt()
                    )
                }
            }
        }
        return ret
    }

    suspend fun getReminder(id: Int): Reminder {
        return reminderRepository.getReminder(id)
    }

    suspend fun updateReminder(reminder: Reminder) {
        reminderRepository.updateReminder(reminder)
        if (reminder.notify) {
            when {
                reminder.reminder_time == (-1).toLong() -> {
                    setNotificationAtPlace(
                        message = reminder.message,
                        id = reminder.id
                    )
                }
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
        state.value.hidden = !state.value.hidden
    }

    private fun getUnhidden() {
        // show reminders in virtual location
        if (state.value.hidden && state.value.location != null) {
            viewModelScope.launch {
                reminderRepository.getRemindersBefore(
                    state.value.location!!.longitude,
                    state.value.location!!.latitude
                ).collect { list ->
                    _state.value = ReminderViewState(
                        reminders = list,
                        hidden = _state.value.hidden,
                        location = _state.value.location
                    )
                }
            }
        }
        // show reminders in real location and not hidden
        else {
            var myLocation: Location?
            if (ActivityCompat.checkSelfPermission(
                    Graph.appContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    Graph.appContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION),
                    1)
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    myLocation = location
                    if (state.value.hidden) {
                        viewModelScope.launch {
                            reminderRepository.getRemindersBefore(
                                myLocation?.longitude ?: 0.0,
                                myLocation?.latitude ?: 0.0
                            ).collect { list ->
                                _state.value = ReminderViewState(
                                    reminders = list,
                                    hidden = _state.value.hidden,
                                    location = _state.value.location
                                )
                            }
                        }
                    } else {
                        viewModelScope.launch {
                            reminderRepository.getReminders().collect { list ->
                                _state.value = ReminderViewState(
                                    reminders = list,
                                    hidden = _state.value.hidden,
                                    location = _state.value.location
                                )
                            }
                        }
                    }
                }
        }
    }
    fun getRealLocation() {
        _state.value = ReminderViewState(
            reminders = _state.value.reminders,
            hidden = _state.value.hidden,
            location = null
        )
        getUnhidden()
    }


    init {
        createNotificationChannel()
        getUnhidden()
    }
}

data class ReminderViewState(
    val reminders: List<Reminder> = emptyList(),
    var hidden: Boolean = true,
    val location: LatLng?
)
