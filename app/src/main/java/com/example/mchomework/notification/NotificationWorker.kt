package com.example.mchomework.notification

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, workerParams: WorkerParameters):
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val message = inputData.getString("msg")
        if (message != null) {
            notifyReminder(message)
        }
        return Result.success()
    }
}