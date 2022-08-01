package com.amrabdelhamiddiab.waiting.presentation.loginflow.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.usecases.login.DownloadOrder
import com.amrabdelhamiddiab.core.usecases.login.DownloadService
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preHelper: IPreferenceHelper,
    private val gson: Gson,
    private val downloadService: DownloadService,
    private val downloadOrder: DownloadOrder

) : ViewModel() {
    private val _userId = SingleLiveEvent<String>()
    val userId: LiveData<String> get() = _userId

    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    private val _orderValue = SingleLiveEvent<Order>()
    val orderValue: LiveData<Order> get() = _orderValue


    fun retrieveUserStateFromPreferences(): Boolean {
        return preHelper.getUserServiceLoggedIn()
    }
    fun getUserIdFromPreferences() {
        _userId.value = preHelper.fetchUserIdForClient()
    }
    fun downloadServiceV(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _service.postValue(downloadService(userId))
        }
    }
    fun downloadOrderForService(){
        viewModelScope.launch {
            val userId = preHelper.fetchUserIdForService()
            _orderValue.postValue(downloadOrder(userId))
        }
    }
    fun saveOrderForServiceInPreferences(oderString: String){
        preHelper.saveOrderForService(oderString)
    }
    fun saveServiceInPreferences(service: Service?) {
        if (service != null) {
            val userServiceString: String? = gson.toJson(service)
            if (userServiceString != null) {
                with(preHelper) { saveServiceForService(userServiceString) }
            }
        }
    }
    fun loadClientOrder(): Order? {
        val orderString =  preHelper.loadOrderForClient()
        return if (orderString.isNotEmpty()) {
            gson.fromJson(orderString, Order::class.java)
        } else {
            null
        }
    }
}
