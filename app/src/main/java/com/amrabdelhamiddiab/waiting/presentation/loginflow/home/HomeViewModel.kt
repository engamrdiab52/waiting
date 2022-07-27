package com.amrabdelhamiddiab.waiting.presentation.loginflow.home

import androidx.lifecycle.ViewModel
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val preHelper: IPreferenceHelper) : ViewModel() {
    fun getUserStateFromPreferences(): Boolean {
        return preHelper.getUserLoggedIn()
    }
}
