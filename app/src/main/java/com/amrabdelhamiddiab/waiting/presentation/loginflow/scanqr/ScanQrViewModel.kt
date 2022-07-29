package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqr

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanQrViewModel @Inject constructor(
    private val prefeHelper: IPreferenceHelper,
    private val gson: Gson
) : ViewModel() {

    private val _navigateOrder = SingleLiveEvent<Boolean>()
    val navigateOrder: LiveData<Boolean> get() = _navigateOrder

    fun saveOrderInPreferences(order: Order){
        val orderClientString: String? = gson.toJson(order)
        if (orderClientString != null) {
            with(prefeHelper) { saveOrderClient(orderClientString) }
        }
    }

    fun saveUserIdInPreferences(userId: String){
        prefeHelper.saveUserId(userId)
    }
    fun retrieveUserIdFromPreferences(): String {
        return prefeHelper.fetchUserId()
    }
    fun navigateToClientFragment(){
        _navigateOrder.value = true
    }
}