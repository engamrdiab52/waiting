package com.amrabdelhamiddiab.waiting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.utilis.TtsProviderFactory
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService :
    FirebaseMessagingService() {

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken
        //  Firebase.messaging.subscribeToTopic(TOPIC)
        Log.d(TAG, "3333333333333333333333" + token.toString())
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        createNotification(message)
        val notificationData = message.data
        val toVoice = notificationData["title"] as String
        Log.d(TAG, "121212121212121212121........"  + toVoice)
        // i guarantee here one instance
        val ttsProviderImpl = TtsProviderFactory.instance
        if (ttsProviderImpl != null) {
            Log.d(TAG, "----------------------ttsProviderImpl != null---------------")
            with(ttsProviderImpl) {

                init(applicationContext, toVoice)
              //  say(toVoice)
               // shutdown()
            }
        }


        //  createVoice(toVoice)
    }


    //here i can pass the importance dependence on a condition on the value i eil receive from sender (service)
    private fun createNotification(message: RemoteMessage) {
        //Create the Intent
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        //Create Pending Intent
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        //Create the Notification
        //here priority will be variable and will change depending on the condition of you are the next
        //Urgent Importance when his number arrives
        //Here I will pass the number of current serving number
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_timelapse)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.waiting_logo))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000))
            .build()

        with(NotificationManagerCompat.from(applicationContext)) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "My channel description"
                enableLights(true)
                lightColor = Color.GREEN
            }
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            createNotificationChannel(channel)
            notify(NOTIFICATION_ID, notification)
        }
        // playNotificationSound(applicationContext)
    }

    private fun playNotificationSound(context: Context) {
        try {
            val defaultSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(context, defaultSoundUri)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val CHANNEL_ID = "notification_channel"
        private const val CHANNEL_NAME = "com.amrabdelhamiddiab.waiting"
        private const val NOTIFICATION_ID = 1023

        var sharedPref: SharedPreferences? = null

        var token: String?
            get() {
                return sharedPref?.getString("token", "")
            }
            set(value) {
                sharedPref?.edit()?.putString("token", value)?.apply()
            }
    }

}