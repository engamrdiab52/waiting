package com.amrabdelhamiddiab.waiting.presentation.loginflow.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.usecases.login.DownloadService
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preHelper: IPreferenceHelper,
    private val downloadService: DownloadService,
    private val auth: FirebaseAuth,

) : ViewModel() {
    private val _userId = SingleLiveEvent<String>()
    val userId: LiveData<String> get() = _userId

    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    fun downloadServiceV() {
        viewModelScope.launch(Dispatchers.IO) {
            _service.postValue(downloadService(auth.currentUser!!.uid))
        }
    }

    fun getClientInAVisit(): Boolean {
        return preHelper.getIfClientInAVisit()
    }

    fun userLoggedIn(): FirebaseUser? {
        return auth.currentUser
    }
}
