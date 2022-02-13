package com.example.mchomework.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mchomework.data.entity.Reminder

@Database(
    entities = [Reminder::class],
    version = 1,
    exportSchema = false
)
abstract class MyDatabase: RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}