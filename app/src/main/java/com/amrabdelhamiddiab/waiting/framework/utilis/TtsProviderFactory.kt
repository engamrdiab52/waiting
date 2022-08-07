package com.amrabdelhamiddiab.waiting.framework.utilis

import android.content.Context


abstract class TtsProviderFactory {
    abstract fun say(sayThis: String?)
    abstract fun init(context: Context?, toVoice: String)
    abstract fun shutdown()

    companion object {
        private var sInstance: TtsProviderFactory? = null
        val instance: TtsProviderFactory?
            get() {
                if (sInstance == null) {
                    try {
                        val className = "TtsProviderImpl"
                        val clazz = Class.forName(
                            TtsProviderFactory::class.java.getPackage()?.name + "." + className
                        )
                            .asSubclass(TtsProviderFactory::class.java)
                        sInstance = clazz.newInstance()
                    } catch (e: Exception) {
                        throw IllegalStateException(e)
                    }
                }
                return sInstance
            }
    }
}