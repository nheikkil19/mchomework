package com.example.mchomework.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mchomework.Graph
import com.example.mchomework.data.entity.Reminder
import com.example.mchomework.data.repository.ReminderRepository
import com.example.mchomework.notification.createNotificationChannel
import com.example.mchomework.notification.deleteNotification
import com.example.mchomework.notification.setNotificationAtTime
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class ReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository
): ViewModel() {
    private val _state = MutableStateFlow(ReminderViewState())

    val state: StateFlow<ReminderViewState>
        get() = _state

    suspend fun saveReminder(reminder: Reminder): Long {
        val ret = reminderRepository.addReminder(reminder)
        if (reminder.notify) {
            setNotificationAtTime(
                delay = reminder.reminder_time - Date().time,
                message = reminder.message,
                id = reminder.id
            )
        }
        return ret
    }

    suspend fun getReminder(id: Int): Reminder? {
        return reminderRepository.getReminder(id)
    }

    suspend fun updateReminder(reminder: Reminder) {
        reminderRepository.updateReminder(reminder)
        if (reminder.notify) {
            setNotificationAtTime(
                delay = reminder.reminder_time - Date().time,
                message = reminder.message,
                id = reminder.id
            )
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
        if (state.value.hidden) {
            viewModelScope.launch {
                reminderRepository.getRemindersBefore().collect { list ->
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

    init {
        createNotificationChannel()
        getUnhidden()
    }
}

data class ReminderViewState(
    val reminders: List<Reminder> = emptyList(),
    val hidden: Boolean = true
)