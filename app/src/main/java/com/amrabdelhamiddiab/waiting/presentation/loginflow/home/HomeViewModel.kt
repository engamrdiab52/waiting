package com.amrabdelhamiddiab.waiting.presentation.loginflow.home

import androidx.lifecycle.ViewModel
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preHelper: IPreferenceHelper,
    private val gson: Gson
) : ViewModel() {
    fun getUserStateFromPreferences(): Boolean {
        return preHelper.getUserLoggedIn()
    }

    fun loadClientOrder(): Order? {
        val orderString =  preHelper.loadOrderClient()
        return if (orderString.isNotEmpty()) {
            gson.fromJson(orderString, Order::class.java)
        } else {
            null
        }
    }
}
