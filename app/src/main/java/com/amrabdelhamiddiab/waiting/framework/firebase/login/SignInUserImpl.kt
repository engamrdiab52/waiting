package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.login.ISignInUser
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignInUserImpl @Inject constructor (private val mAuth: FirebaseAuth): ISignInUser {
    override suspend fun signInUser(email: String, password: String): Boolean {
        return try {
            val authResult = mAuth.signInWithEmailAndPassword(email, password).await()
            if (authResult != null) {
                Log.d(TAG, mAuth.currentUser?.email.toString())
                true
            } else {
                Log.d(TAG, "user didn't sign in")
                false
            }
        } catch (ex: Exception) {
            Log.d(TAG, ex.message.toString())
            false
        }
    }
}