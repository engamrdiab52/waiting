package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanorpickqrcode

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanOrPickQrcodeViewModel @Inject constructor() : ViewModel() {
    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    private val _userId = SingleLiveEvent<String?>()
    val userId: LiveData<String?> get() = _userId

    private val _downloading = SingleLiveEvent<Boolean>()
    val downloading: LiveData<Boolean> get() = _downloading

    //to check it in fragment
    fun checkThisString(string: String){
        _userId.value = string
    }

}