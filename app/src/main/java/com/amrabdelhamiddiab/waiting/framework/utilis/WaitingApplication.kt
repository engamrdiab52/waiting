package com.amrabdelhamiddiab.waiting.framework.utilis

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.MyFirebaseMessagingService
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
        val soundAttributes =
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
        val soundUri: Uri =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/" + "sound1.mp3")

        with(NotificationManagerCompat.from(applicationContext)) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "My channel description"
                enableLights(true)
                lightColor = Color.GREEN
                //  vibrationPattern = longArrayOf(1000, 1000, 1000)
                enableVibration(true)
            }
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
          //  channel.setSound(soundUri, soundAttributes)
         //   channel.setSound(soundUri, soundAttributes)
            createNotificationChannel(channel)
            Log.d(TAG,"-----------------------------------------------"+ channel.sound.path.toString())
        }
    }

    companion object {
        const val CHANNEL_ID = "notification_channel"
        private const val CHANNEL_NAME = "com.amrabdelhamiddiab.waiting"
    }

}