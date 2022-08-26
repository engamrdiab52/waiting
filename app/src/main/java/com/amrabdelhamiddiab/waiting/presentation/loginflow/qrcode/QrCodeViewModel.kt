package com.amrabdelhamiddiab.waiting.presentation.loginflow.qrcode

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    private val auth: FirebaseAuth
) :
    ViewModel() {

    val userId: String
        get() {
            return auth.currentUser!!.uid
        }

}
