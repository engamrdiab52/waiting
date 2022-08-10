package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqr

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.core.usecases.login.DownloadService
import com.amrabdelhamiddiab.core.usecases.login.UploadClientToken
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanQrViewModel @Inject constructor(
    private val prefeHelper: IPreferenceHelper,
    private val gson: Gson,
    private val downloadService: DownloadService,
    private val uploadClientToken: UploadClientToken
) : ViewModel() {


    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    private val _userId = SingleLiveEvent<String?>()
    val userId: LiveData<String?> get() = _userId

    private val _tokenUploaded = SingleLiveEvent<Boolean>()
    val tokenUploaded: LiveData<Boolean> get() = _tokenUploaded

    private val _downloading = SingleLiveEvent<Boolean>()
    val downloading: LiveData<Boolean> get() = _downloading

    fun saveUserIdInPreferences(userId: String){
        prefeHelper.saveUserIdForClient(userId)
    }

    fun downloadServiceV(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _service.postValue(downloadService(userId))
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
        Log.d(MainActivity.TAG,"uploadMyClientToken(token : Token).....called..." + token)
        viewModelScope.launch(Dispatchers.IO) {
            _tokenUploaded.postValue( uploadClientToken(token)!!)
        }
    }
}