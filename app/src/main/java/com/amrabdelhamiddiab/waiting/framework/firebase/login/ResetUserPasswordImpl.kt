package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.login.IResetUserPassword
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject


class ResetUserPasswordImpl @Inject constructor(
    private val mAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) :
    IResetUserPassword {
    override suspend fun resetPassword(email: String): Boolean {
        if (checkInternetConnection(context)) {
            return try {
                mAuth.sendPasswordResetEmail(email).await()
                true
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()
                }
                false
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