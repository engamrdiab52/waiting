package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.login.ISendEmailVerification
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SendEmailVerificationImpl @Inject constructor (private val mAuth: FirebaseAuth): ISendEmailVerification {
    override suspend fun sendEmailVerification(): Boolean {
        return  try {
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
            Log.d(TAG, ex.message.toString())
            false
        }
    }
}