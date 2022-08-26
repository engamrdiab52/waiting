package com.amrabdelhamiddiab.waiting.framework.utilis

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.AudioAttributes
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WaitingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)

        registerWaitingNotificationChannel()
    }

    private fun registerWaitingNotificationChannel() {
        AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
        with(NotificationManagerCompat.from(applicationContext)) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "My channel description"
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
            }
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            createNotificationChannel(channel)
            Log.d(TAG,"-----------------------------------------------"+ channel.sound.path.toString())
        }
    }

    companion object {
        const val CHANNEL_ID = "notification_channel"
        private const val CHANNEL_NAME = "com.amrabdelhamiddiab.waiting"
    }

}