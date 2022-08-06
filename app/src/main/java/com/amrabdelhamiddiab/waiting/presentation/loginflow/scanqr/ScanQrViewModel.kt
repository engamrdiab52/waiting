package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqr

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.core.usecases.login.DownloadService
import com.amrabdelhamiddiab.core.usecases.login.UploadClientToken
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

    private val _tokenUploaded = SingleLiveEvent<Boolean?>()
    val tokenUploaded: LiveData<Boolean?> get() = _tokenUploaded


    private val _navigateOrder = SingleLiveEvent<Boolean>()
    val navigateOrder: LiveData<Boolean> get() = _navigateOrder

    fun saveOrderInPreferences(order: Order){
        val orderClientString: String? = gson.toJson(order)
        if (orderClientString != null) {
            with(prefeHelper) { saveOrderForClient(orderClientString) }
        }
    }

    fun saveUserIdInPreferences(userId: String){
        prefeHelper.saveUserIdForClient(userId)
    }
    fun retrieveUserIdFromPreferences(): String {
        return prefeHelper.fetchUserIdForClient()
    }
    fun navigateToClientFragment(){
        _navigateOrder.value = true
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
    fun uploadMyClientToken(userId: String , token : Token) {

        viewModelScope.launch(Dispatchers.IO) {
            _tokenUploaded.postValue( uploadClientToken(userId , token))
        }
    }
}