package com.amrabdelhamiddiab.waiting.presentation.loginflow

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.usecases.login.*
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginFlowViewModel @Inject constructor(
    private val signUpUser: SignUpUser,
    private val signInUser: SignInUser,
    private val sendEmailVerification: SendEmailVerification,
    private val resetUserPassword: ResetUserPassword,
    private val emailVerifiedState: EmailVerifiedState,
    private val firebaseAuth: FirebaseAuth,
    private val downloadService: DownloadService,
    private val iPreferenceHelper: IPreferenceHelper

) : ViewModel() {

    private val _passwordChanged = SingleLiveEvent<Boolean?>()
    val passwordChanged: LiveData<Boolean?> get() = _passwordChanged

    private val _emailVerificationSent = SingleLiveEvent<Boolean>()
    val emailVerificationSent: LiveData<Boolean> get() = _emailVerificationSent

    private val _userCreated = SingleLiveEvent<Boolean>()
    val userCreated: LiveData<Boolean> get() = _userCreated

    private val _userSignedIn = SingleLiveEvent<Boolean?>()
    val userSignedIn: LiveData<Boolean?> get() = _userSignedIn

    private val _emailVerified = SingleLiveEvent<Boolean>()
    val emailVerified: LiveData<Boolean> get() = _emailVerified

    private val _downloading = SingleLiveEvent<Boolean>()
    val downloading: LiveData<Boolean> get() = _downloading

    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    val mAuth get() = firebaseAuth

    val preferenceHelper get() = iPreferenceHelper
    fun retrieveFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _userSignedIn.postValue(signInUser(email, password))
            _downloading.postValue(false)
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun createUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _userCreated.postValue(signUpUser(email, password))
            _downloading.postValue(false)
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _passwordChanged.postValue(resetUserPassword(email))
            _downloading.postValue(false)
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun sendVerificationEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _emailVerificationSent.postValue(sendEmailVerification())
            _downloading.postValue(false)
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun isEmailVerified() {
        viewModelScope.launch(Dispatchers.IO) {
            _emailVerified.postValue(emailVerifiedState())
        }
    }

    fun downloadServiceV() {
        viewModelScope.launch(Dispatchers.IO) {
            _service.postValue(firebaseAuth.currentUser?.let { downloadService(it.uid) })
        }
    }
}