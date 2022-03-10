package com.example.mchomework.data.repository

import android.location.Location
import android.location.Location.distanceBetween
import android.util.Log
import com.example.mchomework.data.entity.Reminder
import com.example.mchomework.data.room.ReminderDao
import kotlinx.coroutines.flow.*
import java.lang.Thread.sleep
import java.util.*

class ReminderRepository(
    private val reminderDao: ReminderDao
) {
    fun getReminders(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders()
    }
    fun getRemindersBefore(
        position_x: Double,
        position_y: Double
    ): Flow<List<Reminder>> {

        var a = reminderDao.getRemindersBefore(Date().time)
        a = a.map { it.filter { reminder ->
            getDistance(position_x, position_y, reminder) }
        }

        return a
    }
    suspend fun getReminder(id: Int): Reminder {
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


fun getDistance(
    position_x: Double,
    position_y: Double,
    reminder: Reminder
): Boolean {
    val posA = Location("a")
    posA.latitude = position_y
    posA.longitude = position_x

    val posB = Location("b")
    posB.latitude = reminder.location_y
    posB.longitude = reminder.location_x

    val res = posA.distanceTo(posB)
//    Log.d("tag", "posA = $posA posB = $posB result = $res")

    return res < 100 || !reminder.location_bool
}
