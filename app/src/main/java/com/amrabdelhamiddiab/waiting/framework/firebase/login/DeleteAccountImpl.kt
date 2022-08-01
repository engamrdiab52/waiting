package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.login.IDeleteAccount
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class DeleteAccountImpl @Inject constructor(private val mAuth: FirebaseAuth): IDeleteAccount {
    override suspend fun deleteAccount() {

        try {
            mAuth.currentUser?.delete()
            Log.d(TAG,"Delete current user" + FirebaseAuth.getInstance().currentUser.toString())
        } catch (e: Exception) {
            Log.d(TAG, " error in mainActivity  " + e.message.toString())
        }
    }
}