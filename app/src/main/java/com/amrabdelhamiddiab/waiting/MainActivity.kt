package com.amrabdelhamiddiab.waiting

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
        const val TOPIC = "/topics/myTopic"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // Firebase.messaging.isAutoInitEnabled = true
        MyFirebaseMessagingService.sharedPref = getSharedPreferences("sharedPrefToken", Context.MODE_PRIVATE)

    //    FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
    }
}