package com.amrabdelhamiddiab.waiting.framework.utilis

import android.content.ContentResolver
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

import java.io.IOException

class MyMediaPlayer private constructor(private val context: Context) {

    private var mp: MediaPlayer? = null
    private val soundUri: Uri =
        Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/raw/" + "sound")


    @Synchronized
    fun playSound() {
        val instance = getInstance(context)
        if (instance.mp == null) {
            instance.mp = MediaPlayer()
        } else {
            instance.mp!!.reset()
        }
        try {
            instance.mp!!.setDataSource(context, soundUri)
            instance.mp!!.prepare()
            instance.mp!!.setVolume(100f, 100f)
            instance.mp!!.isLooping = false
            instance.mp!!.start()
            instance.mp!!.setOnCompletionListener {
                if (instance.mp != null) {
                    instance.mp!!.reset()
                    instance.mp = null
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object : SingletonHolder<MyMediaPlayer, Context>(::MyMediaPlayer)
}