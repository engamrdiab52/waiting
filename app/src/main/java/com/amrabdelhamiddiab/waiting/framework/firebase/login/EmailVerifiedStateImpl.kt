package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.login.IEmailVerifiedState
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class EmailVerifiedStateImpl @Inject constructor(
    private val mAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) :
    IEmailVerifiedState {
    override suspend fun isEmailVerified(): Boolean {
         if (checkInternetConnection(context)) {
            val result = mAuth.currentUser?.isEmailVerified
             return if (result != null) {
                 if (result == false) {
                     withContext(Dispatchers.Main) {
                         Toast.makeText(context, "please verify your email", Toast.LENGTH_LONG)
                             .show()
                     }
                     false
                 }else {
                     true
                 }
             } else {
                 false
             }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "PLEASE CHECK INTERNET CONNECTION", Toast.LENGTH_LONG)
                    .show()
            }
          return  false
        }
    }
}