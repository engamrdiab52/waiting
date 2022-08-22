package com.amrabdelhamiddiab.waiting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
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
        //here i have to create token reference uid
        //   Firebase.database.reference
        Log.d(TAG, "TOKEN****************************" + token.toString())
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "...............Message Received..................")

        createNotification(message)
        val notificationData = message.data
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val speakNumbersEnabled = sharedPreferences.getBoolean("notification_speak_number", true)
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
    private fun createNotification(message: RemoteMessage) {
        val soundMe: Uri =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/" + "sound1.mp3")
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
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.notification_image))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000,
                    1000,
                    1000
                )
            )/*.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)*/

        val notification = notificationBuilder.build()
        /*     with(NotificationManagerCompat.from(applicationContext)) {
                 val channel = NotificationChannel(
                     CHANNEL_ID, CHANNEL_NAME,
                     NotificationManager.IMPORTANCE_HIGH
                 ).apply {
                     description = "My channel description"
                     enableLights(true)
                     lightColor = Color.GREEN
                     setSound(soundMe, soundAttributes)
                    // vibrationPattern = longArrayOf(1000, 1000, 1000)
                 }
                 channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                 createNotificationChannel(channel)
                 notify(NOTIFICATION_ID, notification)
             }*/
        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
        //   playNotificationSound(applicationContext)
        playCustomSound()
    }

    /*    private fun playNotificationSound(context: Context) {
            try {
                val defaultSoundUri =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(context, defaultSoundUri)
                r.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
    private fun playCustomSound() {
        val soundUri: Uri =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/" + "sound")

        playResource()
    }

    private fun playResource() {
        MyMediaPlayer.getInstance(this).playSound()
        Log.d(
            TAG,
            " private fun playResource() {.................................222222222222222222222222"
        )


/*
        val player = MediaPlayer.create(this, uri)
        try {
            player.prepareAsync()
        }catch (ex: Exception){
            Log.d(TAG, ex.message.toString())
        }
        player.setOnPreparedListener {
            player.start()
        }
        player.setOnCompletionListener {
            player.stop()
            player.reset()
            player.release()
        }
*//*
*

        val player = MediaPlayer.create(this, uri)
        try {
            player.prepareAsync()
        } catch (ex: Exception) {
            Log.d(TAG, ex.message.toString())
        }
        player.setOnPreparedListener {
            player.start()
        }
        player.setOnCompletionListener {
            player.stop()
            player.reset()
            player.release()
        }*//*

        if (player != null) {
            if (player.isPlaying) {
                player.stop()
                player.reset()
                Log.d(TAG, "Media Player................ if (player!!.isPlaying)")
            }
            player.setDataSource(this, uri)
            player.prepare()
        } else {
            try {
                player = MediaPlayer.create(this, uri)
                Log.d(TAG, "Media Player................ MediaPlayer.create(this, uri) )")
            } catch (ex: Exception) {
                Log.d(
                    TAG,
                    "Media Player................ MediaPlayer.create(this, uri) )" + ex.message.toString()
                )
            }
        }
        player!!.start()*/
    }

    companion object {
        /*private const val CHANNEL_ID = "notification_channel"
        private const val CHANNEL_NAME = "com.amrabdelhamiddiab.waiting"*/
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