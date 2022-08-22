/*
package com.amrabdelhamiddiab.waiting.framework.utilis

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import java.io.IOException


*/
/**
 * Created by rajnit on 10/07/17.
 *//*

class MyMediaPlayer private constructor(private val context: Context) {
    var mp: MediaPlayer? = null

    */
/**
     *
     * @param fileName if sound name is "sound.mp3" then pass fileName as "sound" only.
     *//*

    @Synchronized
    fun playSound(fileName: String) {
        if (instance!!.mp == null) {
            instance!!.mp = MediaPlayer()
        } else {
            instance!!.mp!!.reset()
        }
        try {
            instance!!.mp!!.setDataSource(context, Uri.parse(APP_RAW_URI_PATH_1 + fileName))
            instance!!.mp!!.prepare()
            instance!!.mp!!.setVolume(100f, 100f)
            instance!!.mp!!.isLooping = false
            instance!!.mp!!.start()
            instance!!.mp!!.setOnCompletionListener {
                L.print("completeSound: $fileName")
                if (instance!!.mp != null) {
                    instance!!.mp!!.reset()
                    instance!!.mp = null
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun stopSound() {
        if (instance!!.mp != null) {
            instance!!.mp!!.stop()
            instance!!.mp!!.release()
        }
    }

    @Synchronized
    fun pauseSound() {
        if (instance!!.mp != null) {
            instance!!.mp!!.pause()
        }
    }

    @Synchronized
    fun restartSound() {
        if (instance!!.mp != null) {
            instance!!.mp!!.start()
        }
    }

    @Synchronized
    fun playRepeatedSound(fileName: String) {
        if (instance!!.mp == null) {
            instance!!.mp = MediaPlayer()
        } else {
            instance!!.mp!!.reset()
        }
        try {
            instance!!.mp!!.setDataSource(context, Uri.parse(APP_RAW_URI_PATH_1 + fileName))
            instance!!.mp!!.prepare()
            instance!!.mp!!.setVolume(100f, 100f)
            instance!!.mp!!.isLooping = true
            instance!!.mp!!.start()
            instance!!.mp!!.setOnCompletionListener { mp ->
                var mp = mp
                if (mp != null) {
                    mp.reset()
                    mp = null
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        val APP_RAW_URI_PATH_1 =
            java.lang.String.format("android.resource://%s/raw/", BuildConfig.APPLICATION_ID)
        private const val TAG = "MyMediaPlayer"

        @Volatile
        private var instance: MyMediaPlayer? = null
        fun getInstance(context: Context): MyMediaPlayer? {
            if (instance == null) {
                synchronized(MyMediaPlayer::class.java) {
                    if (instance == null) {
                        instance = MyMediaPlayer(context)
                    }
                }
            }
            return instance
        }
    }
}*/
