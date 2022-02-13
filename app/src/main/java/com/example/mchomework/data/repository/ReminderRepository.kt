package com.example.mchomework.data.repository

import com.example.mchomework.data.entity.Reminder
import com.example.mchomework.data.room.ReminderDao
import kotlinx.coroutines.flow.Flow

class ReminderRepository(
    private val reminderDao: ReminderDao
) {

    fun getReminders(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders()
    }
    suspend fun getReminder(id: Int): Reminder? {
        return reminderDao.getReminder(id)
    }
    suspend fun updateReminder(reminder: Reminder) {
        return reminderDao.update(reminder)
    }
    suspend fun deleteReminder(reminder: Reminder) {
        return reminderDao.delete(reminder)
    }

    suspend fun addReminder(reminder: Reminder): Long = reminderDao.insert(reminder)
}