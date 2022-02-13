package com.example.mchomework

import android.content.Context
import androidx.room.Room
import com.example.mchomework.data.room.MyDatabase
import com.example.mchomework.data.repository.ReminderRepository

object Graph {
    lateinit var database: MyDatabase

    val reminderRepository by lazy {
        ReminderRepository(
            reminderDao = database.reminderDao()
        )
    }


    fun provide(context: Context) {
    database = Room.databaseBuilder(context, MyDatabase::class.java, "myData.db")
        .fallbackToDestructiveMigration()
        .build()
    }


}