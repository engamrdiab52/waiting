package com.amrabdelhamiddiab.waiting.presentation.loginflow.createservice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.usecases.login.ChangeOrderValue
import com.amrabdelhamiddiab.core.usecases.login.UploadService
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateFragmentViewModel @Inject constructor(
    private val preHelper: IPreferenceHelper,
    private val gson: Gson,
    private val uploadService: UploadService,
    private val firebaseAuth: FirebaseAuth,
    private val changeOrderValue: ChangeOrderValue
) : ViewModel() {
    fun fetchUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
    fun saveServiceInPreferences(service: Service?) {
        if (service != null) {
            val userServiceString: String? = gson.toJson(service)
            if (userServiceString != null) {
                with(preHelper) { saveServiceForService(userServiceString) }
            }
        }
    }
    fun uploadServiceV(userId: String, service: Service?){
        if (service != null) {
         //   val userId1 = firebaseAuth.currentUser?.uid
            viewModelScope.launch(Dispatchers.IO){
                uploadService(userId, service)
            }
            }
        }
    fun uploadOrderValueFirstTime(userId: String){
        viewModelScope.launch {
            changeOrderValue(userId, Order(0L))
        }
    }

    }