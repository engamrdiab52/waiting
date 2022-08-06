package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.Voice
import android.util.Log
import com.amrabdelhamiddiab.waiting.MainActivity
import java.util.*


class TtsProviderImpl : TtsProviderFactory(), OnInitListener {
    private var tts: TextToSpeech? = null
    private val local = Locale("ar")
    private val voiceName = local.toLanguageTag()
    private val voice = Voice(voiceName, local, Voice.QUALITY_HIGH, Voice.LATENCY_HIGH, false, null)

    override fun init(context: Context?) {
        if (tts == null) {
            tts = TextToSpeech(context, this)
        }
    }

    override fun say(sayThis: String?) {
        tts?.voice = voice
        tts?.speak(sayThis, TextToSpeech.QUEUE_FLUSH, null, "")

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(local)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            } else {
                Log.d(MainActivity.TAG, "TextToSpeech supported")
            }
        }
    }

    override fun shutdown() {
        tts!!.shutdown()
    }
}