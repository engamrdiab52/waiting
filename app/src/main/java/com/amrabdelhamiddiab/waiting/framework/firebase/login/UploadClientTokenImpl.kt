package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.data.login.IUploadClientToken
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UploadClientTokenImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    @ApplicationContext private val context: Context,
    private val preferenceHelper: IPreferenceHelper
) :
    IUploadClientToken {
    override suspend fun uploadTokenValue(token: Token): Boolean {
        val serviceId = preferenceHelper.fetchUserIdForClient()
        Log.d(MainActivity.TAG, " override suspend fun uploadTokenValue.....called" + serviceId)
        if (serviceId.isNotEmpty()) {
            return if (checkInternetConnection(context)) {
                try {
                    val ref = databaseReference.child("tokens").child(serviceId)
                    val savedTokenReferenceId = preferenceHelper.retrieveClientTokenId()
                    if (savedTokenReferenceId.isNotEmpty()){
                        Log.d(TAG,"savedTokenReferenceId...........isNotEmpty......."+ savedTokenReferenceId )
                        ref.child(savedTokenReferenceId).setValue(token).await()
                        true
                    }else {
                        val tokenKey = ref.push().key
                        if (tokenKey != null) {
                            preferenceHelper.saveClientTokenId(tokenKey)
                            Log.d(TAG,"tokenKey...........!= null......."+ tokenKey )
                            ref.child(tokenKey).setValue(token).await()
                            true
                        } else {
                            false
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                    false
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "PLEASE CHECK INTERNET CONNECTION", Toast.LENGTH_LONG)
                        .show()
                }
                false
            }
        } else {
            return false
        }
    }
}
// fun retrieveUserIdFromPreferences(): String {
//        return prefeHelper.fetchUserIdForClient()
//    }