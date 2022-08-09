package com.amrabdelhamiddiab.waiting.presentation.loginflow.createservice

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.usecases.login.ChangeOrderValue
import com.amrabdelhamiddiab.core.usecases.login.UploadService
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateFragmentViewModel @Inject constructor(
    private val preHelper: IPreferenceHelper,
    private val gson: Gson,
    private val uploadService: UploadService,
    private val firebaseAuth: FirebaseAuth,
    private val changeOrderValue: ChangeOrderValue
) : ViewModel() {
    private val _downloading = SingleLiveEvent<Boolean>()
    val downloading: LiveData<Boolean> get() = _downloading

    private val _serviceCreated = SingleLiveEvent<Boolean>()
    val serviceCreated: LiveData<Boolean> get() = _serviceCreated

    fun uploadServiceV(service: Service) {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _serviceCreated.postValue(uploadService(service)!!)
            _downloading.postValue(false)
        }
    }


}