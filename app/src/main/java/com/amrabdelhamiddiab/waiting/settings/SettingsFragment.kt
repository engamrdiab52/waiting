package com.amrabdelhamiddiab.waiting.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.framework.utilis.LocaleHelper

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var notificationEnable: SwitchPreferenceCompat
    private lateinit var notificationSpeakNumber: SwitchPreferenceCompat
    private lateinit var nightMode: SwitchPreferenceCompat
    private lateinit var languageList: ListPreference
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationEnable = preferenceManager.findPreference("notification_enable")!!
        notificationSpeakNumber = preferenceManager.findPreference("notification_speak_number")!!
        nightMode = preferenceManager.findPreference("night_mode")!!
        languageList = preferenceManager.findPreference("language_list")!!
        notificationEnable.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                Toast.makeText(requireContext(), newValue.toString(), Toast.LENGTH_SHORT).show()
                true
            }
        notificationSpeakNumber.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                    Toast.makeText(requireContext(), newValue.toString(), Toast.LENGTH_SHORT).show()
                true
            }
        nightMode.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                if (newValue == true){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                true
            }
        languageList.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->

                when (newValue) {
                    "en" -> {
                        LocaleHelper.updateResources(requireActivity(), "en")
                        requireActivity().recreate()
                    }
                    "ar" -> {
                        LocaleHelper.updateResources(requireActivity(), "ar")
                        requireActivity().recreate()
                    }
                }
                true
            }
        return super.onCreateView(inflater, container, savedInstanceState)

    }
}