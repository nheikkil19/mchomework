package com.example.mchomework.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Reminder::class],
    version = 1,
    exportSchema = false
)
abstract class MyDatabase: RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}