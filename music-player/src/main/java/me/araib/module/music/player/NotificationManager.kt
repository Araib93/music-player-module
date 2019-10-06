package me.araib.module.music.player

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationManager(private val context: Context) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel() {
        val channel =
            NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val builder = NotificationCompat.Builder(context, "default")
            .setContentTitle("Test title")
            .setContentText("Test text")

        val notification = builder.build()
        with(NotificationManagerCompat.from(context)) {
            notify(4000, notification)
        }
    }

    fun updateNotification(music: Music) {
        val builder = NotificationCompat.Builder(context, "default")
            .setContentTitle(music.name)
            .setContentText(when (music.musicType) {
                MusicType.LOCAL -> "Playing local music"
                MusicType.ASSET -> "Playing music from asset"
                MusicType.ONLINE -> "Playing online music"
            })
            .setStyle(NotificationCompat.MediaStyle())

        val notification = builder.build()
    }
}