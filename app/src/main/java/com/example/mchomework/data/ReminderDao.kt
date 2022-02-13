package com.example.mchomework.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReminderDao {

    @Query("SELECT * FROM reminders")
    abstract fun getAllReminders(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE id = :id")
    abstract fun getReminder(id: Int): Reminder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entity: Reminder)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Reminder)

    @Delete
    abstract suspend fun delete(entity: Reminder)
}