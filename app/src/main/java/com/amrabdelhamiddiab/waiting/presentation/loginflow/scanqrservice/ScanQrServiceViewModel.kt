package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqrservice

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.core.usecases.login.UploadClientToken
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanQrServiceViewModel @Inject constructor (
        private val auth: FirebaseAuth,
        private val uploadClientToken: UploadClientToken
        ): ViewModel() {

        private val _clientTokenString = SingleLiveEvent<String?>()
        val clientTokenString: LiveData<String?> get() = _clientTokenString

        private val _tokenUploaded = SingleLiveEvent<Boolean>()
        val tokenUploaded: LiveData<Boolean> get() = _tokenUploaded


        fun takeClientToken(string: String){
                _clientTokenString.value = string
        }
        fun uploadTokenObject(visitorOrder: Int){
                val user = auth.currentUser!!.uid
              val token =  Token(_clientTokenString.value.toString(), visitorOrder)
                viewModelScope.launch(Dispatchers.IO) {
                        _tokenUploaded.postValue( uploadClientToken(token)!!)
                }

        }
}