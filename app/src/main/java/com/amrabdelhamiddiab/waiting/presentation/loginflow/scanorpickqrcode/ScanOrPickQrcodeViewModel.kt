package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanorpickqrcode

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.core.usecases.login.DownloadService
import com.amrabdelhamiddiab.core.usecases.login.UploadClientToken
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanOrPickQrcodeViewModel @Inject constructor(
    private val prefeHelper: IPreferenceHelper,
    private val downloadService: DownloadService,
    private val uploadClientToken: UploadClientToken
) : ViewModel() {
    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    private val _userId = SingleLiveEvent<String?>()
    val userId: LiveData<String?> get() = _userId

    private val _tokenUploaded = SingleLiveEvent<Boolean?>()
    val tokenUploaded: LiveData<Boolean?> get() = _tokenUploaded

    private val _downloading = SingleLiveEvent<Boolean>()
    val downloading: LiveData<Boolean> get() = _downloading

    private val _navigateOrder = SingleLiveEvent<Boolean>()
    val navigateOrder: LiveData<Boolean> get() = _navigateOrder

    fun saveUserIdInPreferences(userId: String){
        prefeHelper.saveUserIdForClient(userId)
    }
    fun downloadServiceV_pick(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _service.postValue(downloadService(userId))
            _downloading.postValue(false)
        }
    }
    //to check it in fragment
    fun checkThisString(string: String){
        _userId.value = string
    }

    fun sayIfClientIsInAVisit(inAVisit: Boolean){
        prefeHelper.setIfClientInAVisit(inAVisit)
    }
    fun uploadMyClientToken(token : Token) {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _tokenUploaded.postValue( uploadClientToken( token))
            _downloading.postValue(false)
        }
    }

}