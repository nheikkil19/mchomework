package com.example.mchomework.data

import kotlinx.coroutines.flow.Flow

class ReminderRepository(
    private val reminderDao: ReminderDao
) {

    fun getReminders(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders()
    }

    suspend fun addReminder(reminder: Reminder) = reminderDao.insert(reminder)
}