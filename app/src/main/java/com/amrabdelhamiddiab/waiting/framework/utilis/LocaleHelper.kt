package com.amrabdelhamiddiab.waiting.framework.utilis

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import java.util.*


object LocaleHelper {
    fun onAttach(context: Context): Context {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val language = sharedPreferences.getString("language_list","en")
        return updateResources(context, language)
    }
     fun updateResources(context: Context, language: String?): Context {
        val locale = Locale(language!!)
        Locale.setDefault(locale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }
}