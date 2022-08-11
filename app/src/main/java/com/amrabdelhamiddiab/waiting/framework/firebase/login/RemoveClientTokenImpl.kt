package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.data.login.IDeleteService
import com.amrabdelhamiddiab.core.data.login.IRemoveClientToken
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.core.Tag
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveClientTokenImpl @Inject constructor(
    private val preferenceHelper: IPreferenceHelper,
    private val databaseReference: DatabaseReference,
    @ApplicationContext private val context: Context
) : IRemoveClientToken {
    override suspend fun removeClientToken(): Boolean {
        if (checkInternetConnection(context)) {
            try {
                val uid = preferenceHelper.fetchUserIdForClient()
                return if (uid.isNotEmpty()) {
                    val ref = databaseReference.child("tokens").child(uid)
                    val tokenKey = preferenceHelper.retrieveClientTokenId()
                    if (tokenKey.isNotEmpty()) {
                        Log.d(TAG,"suspend fun removeClientToken()................" +tokenKey)
                        ref.child(tokenKey).removeValue().await()
                        true
                    } else {
                        Log.d(TAG,"suspend fun removeClientToken():............called ....false" )
                        false
                    }
                }else {
                    false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
                return false
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "PLEASE CHECK INTERNET CONNECTION", Toast.LENGTH_LONG)
                    .show()
            }
            return false
        }
    }
}