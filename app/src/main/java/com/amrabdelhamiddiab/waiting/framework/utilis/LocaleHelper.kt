package com.amrabdelhamiddiab.waiting.framework.utilis

import android.annotation.SuppressLint
import android.os.Build

import android.annotation.TargetApi
import android.content.Context

import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.preference.PreferenceManager
import java.util.*


object LocaleHelper {
 /*    const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
     private const val PREFS_NAME = "CharityPreferences"
*/
    fun onAttach(context: Context): Context {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val language = sharedPreferences.getString("language_list","en")
        return updateResources(context, language)
    }

  /*  fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }*/

 /*    fun setLocale(context: Context, language: String?): Context {
     //   persist(context, language)
        return updateResources(context, language)
     }
*/
/*    private fun getPersistedData(context: Context, defaultLanguage: String): String? {
         val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage)
    }*/

/*    private fun persist(context: Context, language: String?) {
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }*/

     fun updateResources(context: Context, language: String?): Context {
        val locale = Locale(language!!)
        Locale.setDefault(locale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

/*    @SuppressLint("ObsoleteSdkInt")
    private fun updateResourcesLegacy(context: Context, language: String?): Context {
        val locale = Locale(language!!)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        context.applicationContext.createConfigurationContext(configuration)
        resources.displayMetrics.setTo(resources.displayMetrics)

        return context
    }*/
}