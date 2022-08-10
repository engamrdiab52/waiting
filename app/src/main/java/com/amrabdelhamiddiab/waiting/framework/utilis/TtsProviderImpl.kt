package com.amrabdelhamiddiab.waiting.framework.utilis

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import java.util.*


class TtsProviderImpl : TtsProviderFactory(), OnInitListener {
    var tts: TextToSpeech? = null
    private lateinit var local: Locale
    lateinit var toVoice2: String
    private var context2: Context? = null
    override fun init(context: Context?, toVoice: String) {
        context2 = context
        toVoice2 = toVoice
        if (tts == null) {
            tts = TextToSpeech(context, this)
            local = Locale("en")
            val voiceName = local.toLanguageTag()
            val voice1 =
                Voice(voiceName, local, Voice.QUALITY_HIGH, Voice.LATENCY_HIGH, false, null)
            tts!!.voice = voice1
        } else {
            tts!!.speak(toVoice, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(local)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context2, "Languge Not Supported", Toast.LENGTH_SHORT).show()
            } else {
                tts!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                    }
                    override fun onDone(utteranceId: String?) {
                        shutdown()
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        Toast.makeText(context2, utteranceId.toString(), Toast.LENGTH_SHORT).show()
                    }
                })
                tts!!.speak(toVoice2, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        } else {
            Toast.makeText(context2, "Error...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun shutdown() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
            tts = null
        }
    }
}