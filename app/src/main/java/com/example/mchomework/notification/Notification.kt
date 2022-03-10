package com.example.mchomework.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.mchomework.Graph
import com.example.mchomework.MainActivity
import com.example.mchomework.R
import java.util.concurrent.TimeUnit


fun createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "RemindersChannel"
        val descriptionText = "Channel Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("rm", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            Graph.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun notifyReminder(message: String) {
    val context = Graph.appContext
    val notificationId = IdCounter.count()
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_IMMUTABLE)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    }
    val notificationManager: NotificationManager =
        Graph.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    var builder = NotificationCompat.Builder(context, "rm")
        .setSmallIcon(R.drawable.myicon)
        .setContentTitle(message)
        .setContentText(context.getString(R.string.reminderDue))
        .setPriority(NotificationCompat.PRIORITY_MAX)
        // Set the intent that will fire when the user taps the notification
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
        .build()

    notificationManager.notify(notificationId, builder)

}

fun setNotificationAtTime(delay: Long, message: String, id: Int) {
    val data = workDataOf("msg" to message, "id" to id)
    val notificationWorkRequest: OneTimeWorkRequest =
        OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

    WorkManager
        .getInstance(Graph.appContext)
        .enqueueUniqueWork(
            "reminder_$id",
            ExistingWorkPolicy.REPLACE,
            notificationWorkRequest
        )
}

fun setRepeatingNotificationAtTime(
    delay: Long,
    message: String,
    id: Int,
    repeatPeriod: Long
) {
    val data = workDataOf("msg" to message, "id" to id)
    val notificationWorkRequest: PeriodicWorkRequest =
        PeriodicWorkRequestBuilder<NotificationWorker>(repeatPeriod, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

    WorkManager
        .getInstance(Graph.appContext)
        .enqueueUniquePeriodicWork(
            "reminder_$id",
            ExistingPeriodicWorkPolicy.REPLACE,
            notificationWorkRequest
        )
}

fun setNotificationAtPlace(
    message: String,
    id: Int,
) {
    val data = workDataOf("msg" to message, "id" to id)
    val notificationWorkRequest: PeriodicWorkRequest =
        PeriodicWorkRequestBuilder<NotificationWorker>(5, TimeUnit.MINUTES)
            .setInitialDelay(0, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

    WorkManager
        .getInstance(Graph.appContext)
        .enqueueUniquePeriodicWork(
            "reminder_$id",
            ExistingPeriodicWorkPolicy.REPLACE,
            notificationWorkRequest
        )
}

fun deleteNotification(id: Int) {
    val workManager = WorkManager.getInstance(Graph.appContext)
    workManager.cancelUniqueWork("reminder_$id")
}


object IdCounter {
    private var counter = 1
    fun count(): Int = counter++
}