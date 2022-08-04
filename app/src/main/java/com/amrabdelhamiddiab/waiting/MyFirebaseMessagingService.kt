package com.amrabdelhamiddiab.waiting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.amrabdelhamiddiab.waiting"

class MyFirebaseMessagingService : FirebaseMessagingService() {
    //generate the notification

    //attach the created notification to the custom layout

    //show the notification

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            Log.d(TAG, "onMessageReceived ...........................called")
          //  generateNotification(remoteMessage.notification!!.title, remoteMessage.notification!!.body)
        }
    }

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.amrabdelhamiddiab.waiting", R.layout.notification_layout)
        remoteView.setTextViewText(R.id.notification_title, title)
        remoteView.setTextViewText(R.id.notification_message, message)
        remoteView.setImageViewResource(R.id.notification_logo, R.drawable.waiting_logo)
        return remoteView
    }

    private fun generateNotification(title: String?, message: String?) {
        val intent = Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        //Channel ID, Channel Name
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.waiting_logo)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
        builder = builder.setContent(title?.let { message?.let { it1 -> getRemoteView(it, it1) } })

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }


}