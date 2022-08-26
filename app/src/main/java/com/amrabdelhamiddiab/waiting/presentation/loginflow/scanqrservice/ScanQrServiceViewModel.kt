package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqrservice

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanQrServiceViewModel @Inject constructor() : ViewModel() {

        private val _clientTokenString = SingleLiveEvent<String?>()
        val clientTokenString: LiveData<String?> get() = _clientTokenString

        fun takeClientToken(string: String){
                _clientTokenString.value = string
        }
}