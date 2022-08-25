package com.amrabdelhamiddiab.waiting.presentation.loginflow.qrcodeclient

import androidx.lifecycle.ViewModel
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.waiting.MyFirebaseMessagingService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QrCodeClientViewModel @Inject constructor(private val preHelper: IPreferenceHelper) :
    ViewModel() {

/*
    val clientToken: String
        get() {
            if (MyFirebaseMessagingService.token != null) {
                return MyFirebaseMessagingService.token.toString()
            }else{
               return ""
            }
        }*/
}