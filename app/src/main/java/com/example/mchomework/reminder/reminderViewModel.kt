package com.example.mchomework.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mchomework.Graph
import com.example.mchomework.data.entity.Reminder
import com.example.mchomework.data.repository.ReminderRepository
import com.example.mchomework.notification.createNotificationChannel
import com.example.mchomework.notification.setNotificationAtTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class ReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository
): ViewModel() {
    private val _state = MutableStateFlow(ReminderViewState())

    val state: StateFlow<ReminderViewState>
        get() = _state

    suspend fun saveReminder(reminder: Reminder): Long {
        setNotificationAtTime(
            delay = reminder.reminder_time - Date().time,
            message = reminder.message
        )
        return reminderRepository.addReminder(reminder)
    }

    suspend fun getReminder(id: Int): Reminder? {
        return reminderRepository.getReminder(id)
    }

    suspend fun updateReminder(reminder: Reminder) {
        setNotificationAtTime(
            delay = reminder.reminder_time - Date().time,
            message = reminder.message
        )
        return reminderRepository.updateReminder(reminder)
    }
    suspend fun deleteReminder(reminder: Reminder) {
        return reminderRepository.deleteReminder(reminder)
    }

    init {
        createNotificationChannel()
        viewModelScope.launch {
            reminderRepository.getReminders().collect { list ->
                _state.value = ReminderViewState(
                    reminders = list
                )
            }
        }
    }
}

data class ReminderViewState(
    val reminders: List<Reminder> = emptyList()
)