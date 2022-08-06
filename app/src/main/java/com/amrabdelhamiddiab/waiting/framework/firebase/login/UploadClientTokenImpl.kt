package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.login.IUploadClientToken
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UploadClientTokenImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    @ApplicationContext private val context: Context
) :
    IUploadClientToken {
    override suspend fun uploadTokenValue(userId: String, token: Token): Boolean {
        return if (checkInternetConnection(context)) {
            try {
                databaseReference.child("tokens").child(userId).setValue(token).await()
                true
            } catch (e: Exception) {
                Log.d(MainActivity.TAG, e.message.toString())
                false
            }
        } else {
            Toast.makeText(context, "PLEASE CHECK INTERNET CONNECTION", Toast.LENGTH_LONG).show()
            false
        }
    }
}
