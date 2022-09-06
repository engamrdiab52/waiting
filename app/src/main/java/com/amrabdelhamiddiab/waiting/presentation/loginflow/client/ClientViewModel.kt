package com.amrabdelhamiddiab.waiting.presentation.loginflow.client

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.core.usecases.login.DownloadService
import com.amrabdelhamiddiab.core.usecases.login.RemoveClientToken
import com.amrabdelhamiddiab.core.usecases.login.UploadClientToken
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val iPreferenceHelper: IPreferenceHelper,
    private val gson: Gson,
    private val downloadService: DownloadService,
    private val removeClientToken: RemoveClientToken,
    private val uploadClientToken: UploadClientToken
) :
    ViewModel() {
    private val _orderValue = SingleLiveEvent<Order?>()
    val orderValue: LiveData<Order?> get() = _orderValue

    private val _myNumber = SingleLiveEvent<Int>()
    val myNumber: LiveData<Int> get() = _myNumber

    private val _tokenUploaded = SingleLiveEvent<Boolean>()
    val tokenUploaded: LiveData<Boolean> get() = _tokenUploaded

    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    private val _downloading = SingleLiveEvent<Boolean>()
    val downloading: LiveData<Boolean> get() = _downloading

    private val _clientTokenRemoved = SingleLiveEvent<Boolean>()
    val clientTokenRemoved: LiveData<Boolean> get() = _clientTokenRemoved

    private val orderListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            _orderValue.postValue(Order(0L))
        }
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val item = dataSnapshot.getValue(Order::class.java)
            _orderValue.postValue(item)
            if (item != null) {
                saveOrderInPreferences(item)
            }
        }
    }
    @SuppressLint("NullSafeMutableLiveData")
    fun removeClientTokenV() {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _clientTokenRemoved.postValue(removeClientToken())
            _downloading.postValue(false)
        }
    }
    fun notifyWhenOrderChange(userId: String) {
        if (userId.isNotEmpty()) {
            databaseReference.child("orders").child(userId)
                .addValueEventListener(orderListener)
        }
    }

    fun saveOrderInPreferences(order: Order) {
        val orderClientString: String? = gson.toJson(order)
        if (orderClientString != null) {
            with(iPreferenceHelper) { saveOrderForClient(orderClientString) }
        }
    }

    fun retrieveClientNumberFromPreferences() {
        val number = if (iPreferenceHelper.loadClientNumberFromPreferences() == -1) {
            0
        } else {
            iPreferenceHelper.loadClientNumberFromPreferences()
        }
        _myNumber.value = number
    }

    fun saveMyNumberInPreferences(myNumber: Int) {
        iPreferenceHelper.saveClientNumberInPreferences(myNumber)
        _myNumber.value = myNumber
    }

    fun retrieveUserIdFromPreferences(): String {
        return iPreferenceHelper.fetchUserIdForClient()
    }

    fun sayIfClientIsInAVisit(inAVisit: Boolean){
        iPreferenceHelper.setIfClientInAVisit(inAVisit)

    }

    fun sayIfReviewDone(): Boolean{
        return iPreferenceHelper.getReviewViewed()
    }

    fun setReviewDoneStatus(status: Boolean){
        iPreferenceHelper.setReviewViewed(status)
    }

    fun downloadServiceV(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _service.postValue(downloadService(userId))

        }
    }
    @SuppressLint("NullSafeMutableLiveData")
    fun uploadMyClientToken(token : Token) {
        viewModelScope.launch(Dispatchers.IO) {
            _tokenUploaded.postValue(uploadClientToken(token))
        }
    }
}