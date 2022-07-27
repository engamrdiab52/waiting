package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.login.ISignUpUser
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignupUserImpl @Inject constructor (private val mAuth: FirebaseAuth) : ISignUpUser {
    private var isUserCreated: Boolean = false
    override suspend fun signupUser(email: String, password: String): Boolean {
        return   try {
            val authResult = mAuth.createUserWithEmailAndPassword(email, password).await()
            isUserCreated = authResult.user != null
            //    mAuth.currentUser?.sendEmailVerification()?.await()
            //    isEmailVerificationSent = true
            Log.d(TAG, authResult.toString())
            Log.d(TAG, "SignUpUserImpl  : email sent")
            isUserCreated
        } catch (ex: Exception) {
            Log.d(TAG, ex.message.toString())
            isUserCreated = false
            //    isEmailVerificationSent = false
            isUserCreated
        }
    }
}