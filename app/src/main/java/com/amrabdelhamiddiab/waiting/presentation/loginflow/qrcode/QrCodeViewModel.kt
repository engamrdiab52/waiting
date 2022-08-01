package com.amrabdelhamiddiab.waiting.presentation.loginflow.qrcode

import androidx.lifecycle.ViewModel
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    private val preHelper: IPreferenceHelper
) :
    ViewModel() {


    fun retrieveUserIdFromPreferences(): String {
        return preHelper.fetchUserIdForClient()
    }

}
