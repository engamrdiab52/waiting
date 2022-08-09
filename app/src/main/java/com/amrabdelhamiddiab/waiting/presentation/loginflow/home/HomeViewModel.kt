package com.amrabdelhamiddiab.waiting.presentation.loginflow.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.usecases.login.DownloadService
import com.amrabdelhamiddiab.core.usecases.login.EmailVerifiedState
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
    private val emailVerifiedState: EmailVerifiedState

) : ViewModel() {
    private val _userId = SingleLiveEvent<String>()
    val userId: LiveData<String> get() = _userId

    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    private val _emailVerified = SingleLiveEvent<Boolean>()
    val emailVerified: LiveData<Boolean> get() = _emailVerified

    fun downloadServiceV() {
        viewModelScope.launch(Dispatchers.IO) {
            _service.postValue(downloadService(auth.currentUser!!.uid))
        }
    }

    fun isEmailVerified() {
        viewModelScope.launch(Dispatchers.IO) {
            _emailVerified.postValue(emailVerifiedState()!!)
        }
    }

    fun getClientInAVisit(): Boolean {
        return preHelper.getIfClientInAVisit()
    }

    fun userLoggedIn() {
        if (auth.currentUser != null) {
            isEmailVerified()
        } else {
            _emailVerified.value = false
        }

    }
}
