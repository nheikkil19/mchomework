package com.example.mchomework.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mchomework.Graph
import com.example.mchomework.data.repository.ReminderRepository
import com.example.mchomework.data.repository.getDistance
import com.google.android.gms.location.LocationServices

class NotificationWorker(context: Context, workerParams: WorkerParameters):
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val message = inputData.getString("msg")
        // add condition for location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(Graph.appContext)
        if (ActivityCompat.checkSelfPermission(
                Graph.appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                Graph.appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        val reminder = Graph.reminderRepository.getReminder(
            inputData.getInt("id", 0)
        )
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (message != null && getDistance(
                        location?.longitude ?: 0.0,
                        location?.latitude  ?: 0.0,
                        reminder
                    )
                ) {
                    notifyReminder(message)
                }
            }

        return Result.success()
    }
}