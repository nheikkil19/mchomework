package com.example.mchomework.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders"
)
data class Reminder(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "location_x") val location_x: Double = 0.0,
    @ColumnInfo(name = "location_y") val location_y: Double = 0.0,
    @ColumnInfo(name = "location_bool") val location_bool: Boolean,
    @ColumnInfo(name = "reminder_time") val reminder_time: Long,
    @ColumnInfo(name = "creation_time") val creation_time: Long,
    @ColumnInfo(name = "creator_id") val creator_id: Int,
    @ColumnInfo(name = "reminder_seen") val reminder_seen: Boolean,
    @ColumnInfo(name = "notify") val notify: Boolean,
    @ColumnInfo(name = "daily") val daily: Boolean,
    @ColumnInfo(name = "weekly") val weekly: Boolean,
)
