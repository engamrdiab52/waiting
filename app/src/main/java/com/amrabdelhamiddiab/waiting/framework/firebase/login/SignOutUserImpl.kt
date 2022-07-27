package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.login.ISignOutUser
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SignOutUserImpl @Inject constructor (private val mAuth: FirebaseAuth): ISignOutUser {
    override suspend fun signOutUser() {
        try {
            mAuth.signOut()
            Log.d(TAG, "UserSignedOut...${mAuth.currentUser}")
        }catch (ex: Exception){
            Log.d(TAG, ex.message.toString())
        }
    }
}