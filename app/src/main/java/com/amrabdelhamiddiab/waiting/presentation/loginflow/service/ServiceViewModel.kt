package com.amrabdelhamiddiab.waiting.presentation.loginflow.service

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabdelhamiddiab.core.data.login.RepositoryDeleteThisDay
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.PushNotification
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.core.usecases.login.*
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.firebase.fcm.FcmService
import com.amrabdelhamiddiab.waiting.framework.utilis.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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
    private val downloadToken: DownloadToken,
    private val listDownloadTokens: ListDownloadTokens,
    private val databaseReference: DatabaseReference,
    private val deleteThisDay: DeleteThisDay
) : ViewModel() {

    private val _orderValue = SingleLiveEvent<Order?>()
    val orderValue: LiveData<Order?> get() = _orderValue

    val uid: String
        get() {
            return auth.currentUser?.uid ?: ""
        }

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
    val orderChanged: LiveData<Boolean> get() = _orderChanged

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
        Log.d(TAG, "downloadServiceV............................called")
        viewModelScope.launch(Dispatchers.IO) {
            _service.postValue(downloadService(auth.currentUser!!.uid))

        }
    }

    fun downloadTokenV() {
        viewModelScope.launch(Dispatchers.IO) {
            _tokenDownloaded.postValue(downloadToken())
        }
    }

    fun deleteThisDayV() {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _dayEnd.postValue(deleteThisDay()!!)
            _downloading.postValue(false)
        }
    }

    fun downloadListOfTokensV() {
        viewModelScope.launch(Dispatchers.IO) {
            _downloading.postValue(true)
            _listOfDownloadedTokens.postValue(listDownloadTokens())
            _downloading.postValue(false)
        }
    }

    fun changeOrderValueV(value: Long) {
        val order = Order(value)
        viewModelScope.launch {
            _downloading.postValue(true)
            _orderChanged.postValue(changeOrderValue(order)!!)
            _downloading.postValue(false)
        }
    }

    fun incrementCurrentOrderValue(value: String) {
        changeOrderValueV(value.toLong() + 1L)
    }

    fun decrementCurrentOrderValue(value: String) {
        if (value.toInt() >= 1) {
            changeOrderValueV(value.toLong() - 1L)
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

    // service here send fcm to the client
    fun sendNotification(pushNotification: PushNotification) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //i don't need live data here
                val response = fcmService.postNotification(pushNotification)
                if (response.isSuccessful) {
                    Log.d(
                        MainActivity.TAG,
                        "7777777777777888888888888888888888888888888" + "Response: ${gson.toJson(response.body())}"
                    )
                } else {
                    Log.e(
                        MainActivity.TAG,
                        "77777777777777777777777777777777777777" + response.raw()
                    )
                }
            } catch (e: Exception) {
                Log.e(MainActivity.TAG, e.toString())
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

    //****************************
    //Listener to the list of tokens on Child:
    /*   private val childInListOfTokensListener = object : ChildEventListener {

           private var _listOfTokens: MutableList<Token> = mutableListOf()

           override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
               val token = snapshot.getValue(Token::class.java)
               if (token != null ){
                   _listOfTokens.clear()
                   if (listOfDownloadedTokens.value != null){
                       _listOfTokens.addAll(listOfDownloadedTokens.value!!)
                       Log.d(TAG, "_listOfTokens ...... != null.....111.....$_listOfTokens")
                       _listOfTokens.add(token)
                       _listOfDownloadedTokens.value = emptyList()
                       _listOfDownloadedTokens.value = _listOfTokens
                       Log.d(TAG, "_listOfTokens ...... != null.....222.....$_listOfTokens")
                   }else {
                       _listOfTokens.add(token)
                       _listOfDownloadedTokens.value = emptyList()
                       _listOfDownloadedTokens.value = _listOfTokens
                       Log.d(TAG, "_listOfTokens ...... == null.........$_listOfTokens")
                   }

               }

           }

           override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
               val token = snapshot.getValue(Token::class.java)
               _listOfTokens.clear()
               if (token != null){
                   _listOfTokens = listOfDownloadedTokens.value as MutableList<Token>
                   _listOfTokens.filterNot { it.token.equals(token) }
                   _listOfDownloadedTokens.value = _listOfTokens
               }
           }

           override fun onChildRemoved(snapshot: DataSnapshot) {

           }

           override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

           }

           override fun onCancelled(error: DatabaseError) {

           }
       }*/

    fun notifyWhenListOfTokensChanged() {
        if (uid.isNotEmpty()) {
            databaseReference.child("tokens").child(uid)
                .addValueEventListener(listOfTokensListener)
        }
    }

    fun notifyWhenOrderChange() {
        if (uid.isNotEmpty()) {
            databaseReference.child("orders").child(uid)
                .addValueEventListener(orderListener)
        }
    }
}
