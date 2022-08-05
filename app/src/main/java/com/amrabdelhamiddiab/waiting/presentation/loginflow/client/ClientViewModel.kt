package com.amrabdelhamiddiab.waiting.presentation.loginflow.client

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.usecases.login.DownloadService
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
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
    private val prefeHelper: IPreferenceHelper,
    private val gson: Gson,
    private val downloadService: DownloadService,
) :
    ViewModel() {

    private val _orderValue = SingleLiveEvent<Order?>()
    val orderValue: LiveData<Order?> get() = _orderValue

    private val _myNumber = SingleLiveEvent<Int>()
    val myNumber: LiveData<Int> get() = _myNumber

    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    private val orderListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            // error
            Log.d(TAG, error.message)
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


    fun notifyWhenOrderChange(userId: String) {
        //    val userId = prefeHelper.fetchUserId()
        // val userId = "C8UkQhcYCRRzqqtCFf5MtDqR9Cq2"
        if (userId.isNotEmpty()) {
            databaseReference.child("orders").child(userId)
                .addValueEventListener(orderListener)
        }
        Log.d(TAG, "22222222222222222222222222notifyWhenOrderChange222222222222222222222222222")
    }

    fun saveOrderInPreferences(order: Order) {
        val orderClientString: String? = gson.toJson(order)
        if (orderClientString != null) {
            with(prefeHelper) { saveOrderForClient(orderClientString) }
        }
    }

    fun retrieveClientNumberFromPreferences() {
        val number = if (prefeHelper.loadClientNumberFromPreferences() == -1) {
            0
        } else {
            prefeHelper.loadClientNumberFromPreferences()
        }
        _myNumber.value = number
    }

    fun saveMyNumberInPreferences(myNumber: Int) {
        prefeHelper.saveClientNumberInPreferences(myNumber)
    }

    fun retrieveUserIdFromPreferences(): String {
        return prefeHelper.fetchUserIdForClient()
    }

    fun sayIfClientIsInAVisit(inAVisit: Boolean){
        prefeHelper.setIfClientInAVisit(inAVisit)

    }
    fun downloadServiceV(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _service.postValue(downloadService(userId))

        }
    }
}