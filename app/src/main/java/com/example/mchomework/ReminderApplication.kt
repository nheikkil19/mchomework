package com.example.mchomework

import android.app.Application
import android.content.Context
import com.example.mchomework.Graph.provide

class ReminderApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        provide(this)
    }
}