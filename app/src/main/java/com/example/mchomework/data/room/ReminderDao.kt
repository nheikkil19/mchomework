package com.example.mchomework.data.room

import androidx.room.*
import com.example.mchomework.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReminderDao {

    @Query("SELECT * FROM reminders")
    abstract fun getAllReminders(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE reminder_time < :date")
    abstract fun getRemindersBefore(date: Long): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE id = :id")
    abstract suspend fun getReminder(id: Int): Reminder

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Reminder): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Reminder)

    @Delete
    abstract suspend fun delete(entity: Reminder)
}