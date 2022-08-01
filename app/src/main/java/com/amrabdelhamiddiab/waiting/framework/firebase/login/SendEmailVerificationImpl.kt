package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.login.ISendEmailVerification
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendEmailVerificationImpl @Inject constructor(
    private val mAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ISendEmailVerification {
    override suspend fun sendEmailVerification(): Boolean {
        return try {
            val user = mAuth.currentUser
            if (user != null) {
                user.sendEmailVerification().await()
                Log.d(TAG, "SendEmailVerificationImpl :  Email sent")
                true
            } else {
                Log.d(TAG, "SendEmailVerificationImpl  : NO users signed in")
                false
            }
        } catch (ex: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()
            }
            false
        }
    }
}