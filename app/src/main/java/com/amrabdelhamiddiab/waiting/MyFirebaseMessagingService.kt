package com.amrabdelhamiddiab.waiting

import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.amrabdelhamiddiab.waiting.framework.utilis.MyMediaPlayer
import com.amrabdelhamiddiab.waiting.framework.utilis.TtsProviderFactory
import com.amrabdelhamiddiab.waiting.framework.utilis.WaitingApplication.Companion.CHANNEL_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService :
    FirebaseMessagingService() {
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken
      }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
         val notificationData = message.data
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val notificationSoundEnabled = sharedPreferences.getBoolean("notification_enable", true)
        val speakNumbersEnabled = sharedPreferences.getBoolean("notification_speak_number", true)
        createNotification(message, notificationSoundEnabled)
        if (speakNumbersEnabled) {
            val toVoice = notificationData["message"] as String
            val ttsProviderImpl = TtsProviderFactory.instance
            if (ttsProviderImpl != null) {
                with(ttsProviderImpl) {
                    init(applicationContext, toVoice)
                }
            }
        }

    }

    //here i can pass the importance dependence on a condition on the value i eil receive from sender (service)
    private fun createNotification(message: RemoteMessage, soundEnabled:Boolean) {
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
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_timelapse)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.notification_icon))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000,
                    1000,
                    1000
                )
            )

        val notification = notificationBuilder.build()
        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
        //   playNotificationSound(applicationContext)
      if (soundEnabled){
          playCustomSound()
      }
    }

    private fun playCustomSound() {
            playResource()
    }

    private fun playResource() {
        MyMediaPlayer.getInstance(this).playSound()
    }

    companion object {
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