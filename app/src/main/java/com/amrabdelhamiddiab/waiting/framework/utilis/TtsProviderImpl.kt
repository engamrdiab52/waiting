package com.amrabdelhamiddiab.waiting.framework.utilis

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import java.util.*


class TtsProviderImpl : TtsProviderFactory(), OnInitListener {
    var tts: TextToSpeech? = null
    private lateinit var local : Locale
    lateinit var toVoice2 :String
    override fun init(context: Context?, toVoice: String) {
        toVoice2 =toVoice
        if (tts == null) {
            tts = TextToSpeech(context, this)
            tts!!.setOnUtteranceProgressListener(object : UtteranceProgressListener(){
                override fun onStart(utteranceId: String?) {
                    Log.d(TAG, "onStart Called")
                }

                override fun onDone(utteranceId: String?) {
                    Log.d(TAG, "onDone Called")
                }

                override fun onError(utteranceId: String?) {
                    Log.d(TAG, "onError Called")
                }

            })
            local = Locale("ar")
            val voiceName = local.toLanguageTag()
            val voice1 = Voice(voiceName, local, Voice.QUALITY_HIGH, Voice.LATENCY_HIGH, false, null)
            tts!!.voice = voice1
            tts!!.speak(toVoice, TextToSpeech.QUEUE_FLUSH, null, "")
            Log.d(TAG, "................init     null................")
        }else {
            tts!!.speak(toVoice, TextToSpeech.QUEUE_FLUSH, null, "")
            Log.d(TAG, "................init   NOT  null................")
        }
    }

    override fun say(sayThis: String?) {
        tts!!.speak(sayThis, TextToSpeech.QUEUE_FLUSH, null, "")
        //shutdown()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(local)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts!!.speak(toVoice2, TextToSpeech.QUEUE_FLUSH, null, "")
                Log.e("TTS", "The Language not supported!")
            } else {
                Log.d(MainActivity.TAG, "TextToSpeech supported")
            }
        }
    }

    override fun shutdown() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        Log.d(TAG, "SHUTDOWN")
    }
}