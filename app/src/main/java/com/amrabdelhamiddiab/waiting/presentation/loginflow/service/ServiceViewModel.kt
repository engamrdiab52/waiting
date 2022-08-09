package com.amrabdelhamiddiab.waiting.presentation.loginflow.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.PushNotification
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.core.usecases.login.*
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.framework.firebase.fcm.FcmService
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val signOutUser: SignOutUser,
    private val downloadService: DownloadService,
    private val deleteAccount: DeleteAccount,
    private val deleteService: DeleteService,
    private val deleteCurrentOrder: DeleteCurrentOrder,
    private val auth: FirebaseAuth,
    private val changeOrderValue: ChangeOrderValue,
    private val downloadOrder: DownloadOrder,
    private val fcmService: FcmService,
    private val gson: Gson,
    private val downloadToken: DownloadToken
) : ViewModel() {

    private val _orderValue = SingleLiveEvent<Order?>()
    val orderValue: LiveData<Order?> get() = _orderValue
    val uid: String
        get() {
            return auth.currentUser!!.uid
        }

    private val _tokenDownloaded = SingleLiveEvent<Token?>()
    val tokenDownloaded: LiveData<Token?> get() = _tokenDownloaded

    private val _userDeleted = SingleLiveEvent<Boolean>()
    val userDeleted: LiveData<Boolean> get() = _userDeleted

    private val _serviceDeleted = SingleLiveEvent<Boolean>()
    val serviceDeleted: LiveData<Boolean> get() = _serviceDeleted

    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    //********************************
    //ok
    private val _downloading = SingleLiveEvent<Boolean>()
    val downloading: LiveData<Boolean> get() = _downloading

    private val _userSignedOut = SingleLiveEvent<Boolean?>()
    val userSignedOut: LiveData<Boolean?> get() = _userSignedOut
    //************************************
    //*************************
    //ok
    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _userSignedOut.postValue(signOutUser())
            _downloading.postValue(false)
        }
    }
    //******************************

    fun downloadServiceV() {
        viewModelScope.launch(Dispatchers.IO) {
            _service.postValue(downloadService(auth.currentUser!!.uid))

        }
    }

    fun downloadOrderV() {
        viewModelScope.launch(Dispatchers.IO) {
            _orderValue.postValue(downloadOrder(auth.currentUser!!.uid))

        }
    }

    fun downloadTokenV() {
        viewModelScope.launch(Dispatchers.IO) {
            _tokenDownloaded.postValue(downloadToken(auth.currentUser!!.uid))

        }
    }

    fun changeOrderValueV(value: Long) {
        val order = Order(value)
        viewModelScope.launch {
            changeOrderValue(auth.currentUser!!.uid, order)

        }
    }

    fun incrementCurrentOrderValue(value: String) {
        val order = Order(value.toLong() + 1)
        viewModelScope.launch {
            changeOrderValue(auth.currentUser!!.uid, order)
            _orderValue.postValue(downloadOrder(auth.currentUser!!.uid))
        }
    }

    fun decrementCurrentOrderValue(value: String) {
        val order = Order(value.toLong() - 1)
        viewModelScope.launch {
            changeOrderValue(auth.currentUser!!.uid, order)
            _orderValue.postValue(downloadOrder(auth.currentUser!!.uid))
        }
    }

    fun deleteAccountV(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
        //    auth.currentUser?.let { deleteService(it.uid) }
        //    auth.currentUser?.let { deleteCurrentOrder(it.uid) }
            _downloading.postValue(true)
            _userDeleted.postValue(deleteAccount(password)!!)
            _downloading.postValue(false)
        }
    }
    fun deleteServiceV() {
        viewModelScope.launch(Dispatchers.IO) {
            //    auth.currentUser?.let { deleteService(it.uid) }
            //    auth.currentUser?.let { deleteCurrentOrder(it.uid) }
            _downloading.postValue(true)
            _serviceDeleted.postValue(deleteService()!!)
            _downloading.postValue(false)
        }
    }

    fun sendNotification(pushNotification: PushNotification) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                //i don't need live data here
                val response = fcmService.postNotification(pushNotification)
                if (response.isSuccessful) {
                    Log.d(MainActivity.TAG, "Response: ${gson.toJson(response.body())}")
                } else {
                    Log.e(MainActivity.TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(MainActivity.TAG, e.toString())
            }

        }
    }

}
