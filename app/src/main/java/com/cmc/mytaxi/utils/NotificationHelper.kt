package com.cmc.mytaxi.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cmc.mytaxi.R


class NotificationHelper(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        val channel = NotificationChannel(
            "fare_channel",
            "Ride Fare Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    fun sendFareNotification(fare: Double, distance: Double, time: Long) {
        val formattedValue = String.format("%.2f", fare)
        val formattedValue2 = String.format("%.2f", distance)
        val notification = NotificationCompat.Builder(context, "fare_channel")
            .setContentTitle("Ride Completed")
            .setContentText("Fare: $formattedValue DH | Distance: $formattedValue2 km | Time: $time min")
            .setSmallIcon(R.drawable.ic_taxi)
            .build()

        notificationManager.notify(1, notification)
    }
}
