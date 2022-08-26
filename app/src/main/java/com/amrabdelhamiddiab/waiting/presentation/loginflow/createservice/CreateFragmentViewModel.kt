package com.amrabdelhamiddiab.waiting.presentation.loginflow.createservice

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.usecases.login.UploadService
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateFragmentViewModel @Inject constructor(
    private val uploadService: UploadService
) : ViewModel() {
    private val _downloading = SingleLiveEvent<Boolean>()
    val downloading: LiveData<Boolean> get() = _downloading

    private val _serviceCreated = SingleLiveEvent<Boolean>()
    val serviceCreated: LiveData<Boolean> get() = _serviceCreated

    @SuppressLint("NullSafeMutableLiveData")
    fun uploadServiceV(service: Service) {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _serviceCreated.postValue(uploadService(service))
            _downloading.postValue(false)
        }
    }
}