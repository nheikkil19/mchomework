package com.example.mchomework.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.example.mchomework.Graph
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
    var notificationId = IdCounter.count()

    var builder = NotificationCompat.Builder(context, "rm")
        .setSmallIcon(R.drawable.myicon)
        .setContentTitle(message)
        .setContentText("Your reminder is due.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        // notificationId is a unique int for each notification that you must define
        notify(notificationId, builder.build())
    }

}

fun setNotificationAtTime(delay: Long, message: String) {
    val data = workDataOf("msg" to message)
    val notificationWorkRequest: WorkRequest =
        OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

    WorkManager
        .getInstance(Graph.appContext)
        .enqueue(notificationWorkRequest)
}


object IdCounter {
    private var counter = 0
    fun count(): Int = counter++
}