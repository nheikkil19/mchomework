package com.example.mchomework.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, workerParams: WorkerParameters):
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val message = inputData.getString("msg")
        if (message != null) {
            notifyReminder(message)
        }
        return Result.success()
    }
}