package com.amrabdelhamiddiab.waiting.presentation.loginflow.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.usecases.login.DownloadService
import com.amrabdelhamiddiab.core.usecases.login.SignOutUser
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val signOutUser: SignOutUser,
    private val preHelper: IPreferenceHelper,
    private val gson: Gson,
    private val downloadService: DownloadService,
    private val databaseReference: DatabaseReference
) : ViewModel() {
    //
    private val _downloading = SingleLiveEvent<Boolean>()
    val downloading: LiveData<Boolean> get() = _downloading

    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            signOutUser()
        }
    }

    fun removeUserFromPreferences() {
        preHelper.setUserLoggedIn(false)
    }

    fun saveServiceInPreferences(service: Service?) {
        if (service != null) {
            val userServiceString: String? = gson.toJson(service)
            if (userServiceString != null) {
                with(preHelper) { saveService(userServiceString) }
            }
        }
    }

    fun removeServiceFromPreferences() {
        val service = Service("", "", "", 0)
        val userServiceString: String? = gson.toJson(service)
        if (userServiceString != null) {
            with(preHelper) { saveService(userServiceString) }
        }
    }

    fun loadServiceFromPreferences(): Service? {
        val serviceString: String = preHelper.loadService()
        return if (serviceString.isNotEmpty()) {
            gson.fromJson(serviceString, Service::class.java)
        } else {
            null
        }
    }

    fun downloadServiceV() {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _service.postValue(downloadService())
            _downloading.postValue(false)

        }
    }
}
