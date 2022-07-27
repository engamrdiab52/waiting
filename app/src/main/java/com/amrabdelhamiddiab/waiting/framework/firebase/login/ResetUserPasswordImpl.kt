package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import com.amrabdelhamiddiab.core.data.login.IResetUserPassword
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class ResetUserPasswordImpl @Inject constructor(
    private val mAuth: FirebaseAuth,
   @ApplicationContext private val context: Context
) :
    IResetUserPassword {
    override suspend fun resetPassword(email: String): Boolean {
        return if (checkInternetConnection(context)) {
            val result = withTimeoutOrNull(3000L) {
                try {
                    mAuth.sendPasswordResetEmail(email).await()
                    true
                } catch (ex: Exception) {
                    Log.d(TAG, "ResetPasswordImpl : " + ex.message.toString())
                    false
                }
            }
            result ?: false
        } else {
            Log.d(TAG, "ResetPasswordImpl : No Network")
            false
        }
    }

}