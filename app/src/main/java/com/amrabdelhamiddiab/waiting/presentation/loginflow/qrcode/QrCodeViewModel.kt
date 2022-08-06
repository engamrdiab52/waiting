package com.amrabdelhamiddiab.waiting.presentation.loginflow.qrcode

import androidx.lifecycle.ViewModel
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    private val preHelper: IPreferenceHelper,
    private val auth: FirebaseAuth
) :
    ViewModel() {


    fun retrieveUserIdFromPreferences(): String {
        return preHelper.fetchUserIdForClient()
    }

    val userId: String
        get() {
            return auth.currentUser!!.uid
        }

}
