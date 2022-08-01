package com.amrabdelhamiddiab.waiting.presentation.loginflow.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.usecases.login.*
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
    private val iPreferenceHelper: IPreferenceHelper,
    private val gson: Gson,
    private val downloadService: DownloadService,
    private val deleteAccount: DeleteAccount,
    private val deleteService: DeleteService,
    private val deleteCurrentOrder: DeleteCurrentOrder,
    private val auth: FirebaseAuth,
    private val changeOrderValue: ChangeOrderValue,
    private val downloadOrder: DownloadOrder
) : ViewModel() {

    private val _orderValue = SingleLiveEvent<Order>()
    val orderValue: LiveData<Order> get() = _orderValue


    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            signOutUser()
        }
    }

    fun removeUserFromPreferences() {
        iPreferenceHelper.setUserServiceLoggedIn(false)
    }

    fun saveServiceInPreferences(service: Service?) {
        if (service != null) {
            val userServiceString: String? = gson.toJson(service)
            if (userServiceString != null) {
                with(iPreferenceHelper) { saveServiceForService(userServiceString) }
            }
        }
    }

    fun removeServiceFromPreferences() {
        val service = Service("", "", "", 0)
        val userServiceString: String? = gson.toJson(service)
        if (userServiceString != null) {
            with(iPreferenceHelper) { saveServiceForService(userServiceString) }
        }
    }

    fun loadServiceFromPreferences(): Service? {
        val serviceString: String = iPreferenceHelper.loadServiceForService()
        return if (serviceString.isNotEmpty()) {
            gson.fromJson(serviceString, Service::class.java)
        } else {
            null
        }
    }

    fun downloadServiceV() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = iPreferenceHelper.fetchUserIdForClient()
            _service.postValue(downloadService(userId))

        }
    }
    fun downloadOrderV() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = iPreferenceHelper.fetchUserIdForClient()
            _orderValue.postValue(downloadOrder(userId))

        }
    }

    fun changeOrderValueV(value: Long) {
        val order = Order(value)
        val userId = iPreferenceHelper.fetchUserIdForClient()
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                changeOrderValue(userId, order)
            }

        }
    }

    fun deleteAccountV() {
        viewModelScope.launch(Dispatchers.IO) {
            auth.currentUser?.let { deleteService(it.uid) }
            auth.currentUser?.let { deleteCurrentOrder(it.uid) }
            deleteAccount()
        }
    }

    fun saveCurrentOrderOfService(currentOrder: String) {
        iPreferenceHelper.saveOrderForService(currentOrder)
    }

    fun retrieveCurrentOrderOfService(): String {
        return iPreferenceHelper.loadOrderForService()
    }
}
