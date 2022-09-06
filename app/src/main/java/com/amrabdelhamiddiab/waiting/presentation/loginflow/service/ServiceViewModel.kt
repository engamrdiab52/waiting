package com.amrabdelhamiddiab.waiting.presentation.loginflow.service

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.PushNotification
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.core.usecases.login.*
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.firebase.fcm.FcmService
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
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
class ServiceViewModel @Inject constructor(
    private val signOutUser: SignOutUser,
    private val downloadService: DownloadService,
    private val deleteAccount: DeleteAccount,
    private val deleteService: DeleteService,
    private val auth: FirebaseAuth,
    private val changeOrderValue: ChangeOrderValue,
    private val fcmService: FcmService,
    private val gson: Gson,
    private val databaseReference: DatabaseReference,
    private val deleteThisDay: DeleteThisDay,
    private val iPreferenceHelper: IPreferenceHelper
) : ViewModel() {

    private val _orderValue = SingleLiveEvent<Order?>()
    val orderValue: LiveData<Order?> get() = _orderValue

    val uid: String
        get() {
            return auth.currentUser?.uid ?: ""
        }
    val preferenceHelper: IPreferenceHelper
        get() {
            return iPreferenceHelper
        }
    val firebaseAuth :FirebaseAuth
    get() = auth

    private val _tokenDownloaded = SingleLiveEvent<Token?>()
    val tokenDownloaded: LiveData<Token?> get() = _tokenDownloaded

    private val _listOfDownloadedTokens = SingleLiveEvent<List<Token>?>()
    val listOfDownloadedTokens: LiveData<List<Token>?> get() = _listOfDownloadedTokens

    private val _userDeleted = SingleLiveEvent<Boolean>()
    val userDeleted: LiveData<Boolean> get() = _userDeleted

    private val _dayEnd = SingleLiveEvent<Boolean>()
    val dayEnd: LiveData<Boolean> get() = _dayEnd

    private val _serviceDeleted = SingleLiveEvent<Boolean>()
    val serviceDeleted: LiveData<Boolean> get() = _serviceDeleted

    private val _dataBaseError = SingleLiveEvent<DatabaseError?>()
    val dataBaseError: LiveData<DatabaseError?> get() = _dataBaseError

    private val _service = SingleLiveEvent<Service?>()
    val service: LiveData<Service?> get() = _service

    private val _orderChanged = SingleLiveEvent<Boolean>()

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

    fun signOutFromGoogle() {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _userSignedOut.postValue(true)
            _downloading.postValue(false)
        }
    }
    fun signOutFromFacebook() {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _userSignedOut.postValue(true)
            _downloading.postValue(false)
        }
    }
    //******************************

    fun downloadServiceV() {
        Log.d(TAG, "downloadServiceV............................called")
        viewModelScope.launch(Dispatchers.IO) {
            _service.postValue(downloadService(auth.currentUser!!.uid))

        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun deleteThisDayV() {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _dayEnd.postValue(deleteThisDay())
            _downloading.postValue(false)
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun changeOrderValueV(value: Long) {
        val order = Order(value)
        viewModelScope.launch {
            _downloading.postValue(true)
            _orderChanged.postValue(changeOrderValue(order))
            _downloading.postValue(false)
        }
    }

    fun incrementCurrentOrderValue(value: String) {
        changeOrderValueV(value.toLong() + 1L)
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun deleteAccountV(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _userDeleted.postValue(deleteAccount(password))
            _downloading.postValue(false)
        }
    }
    fun deleteAccountFromGoogle() {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _userDeleted.postValue(true)
            _downloading.postValue(false)
        }
    }
    fun deleteAccountFromFacebook() {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _userDeleted.postValue(true)
            _downloading.postValue(false)
        }
    }
    @SuppressLint("NullSafeMutableLiveData")
    fun deleteServiceV() {
        viewModelScope.launch(Dispatchers.IO) {
            //    auth.currentUser?.let { deleteService(it.uid) }
            //    auth.currentUser?.let { deleteCurrentOrder(it.uid) }
            _downloading.postValue(true)
            _serviceDeleted.postValue(deleteService())
            _downloading.postValue(false)
        }
    }

    // service here send fcm to the client
    fun sendNotification(pushNotification: PushNotification) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //i don't need live data here
                val response = fcmService.postNotification(pushNotification)
                if (response.isSuccessful) {
                    Log.d(TAG, "Response: ${gson.toJson(response.body())}")
                } else {
                    Log.e(TAG, response.raw().message())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }

        }
    }

    //**********************
    //Listener to Services
    private val serviceListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            _dataBaseError.postValue(error)
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val item = dataSnapshot.getValue(Service::class.java)
            _service.postValue(item)
        }
    }

    //****************************
    fun notifyWhenServiceChange() {
        if (uid.isNotEmpty()) {
            databaseReference.child("services").child(uid)
                .addValueEventListener(serviceListener)
        }
    }

    //******************************************
    //Listener to Orders
    private val orderListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            _dataBaseError.postValue(error)
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val item = dataSnapshot.getValue(Order::class.java)
            _orderValue.postValue(item)
        }
    }

    //****************************
    //Listener to the list of tokens
    private val listOfTokensListener = object : ValueEventListener {
        private val _listOfTokens: MutableList<Token> = mutableListOf()
        override fun onDataChange(snapshot: DataSnapshot) {
            //here you will trigger the observer with empty list
            _listOfDownloadedTokens.value = emptyList()
            _listOfTokens.clear()
            snapshot.children.forEach {
                val token = it.getValue(Token::class.java)
                if (token != null) {
                    _listOfTokens.add(token)
                }
            }
            _listOfDownloadedTokens.value = _listOfTokens
        }

        override fun onCancelled(error: DatabaseError) {
            _listOfDownloadedTokens.value = emptyList()
        }
    }

    fun notifyWhenListOfTokensChanged() {
        if (uid.isNotEmpty()) {
            databaseReference.child("tokens").child(uid)
                .addValueEventListener(listOfTokensListener)
        }
    }

    fun sayIfReviewDone(): Boolean {
        return iPreferenceHelper.getReviewViewed()
    }

    fun setReviewDoneStatus(status: Boolean) {
        iPreferenceHelper.setReviewViewed(status)
    }

    fun notifyWhenOrderChange() {
        if (uid.isNotEmpty()) {
            databaseReference.child("orders").child(uid)
                .addValueEventListener(orderListener)
        }
    }
}
