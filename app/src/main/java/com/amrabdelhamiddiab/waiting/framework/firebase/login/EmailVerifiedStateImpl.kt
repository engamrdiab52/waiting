package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import com.amrabdelhamiddiab.core.data.login.IEmailVerifiedState
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class EmailVerifiedStateImpl @Inject constructor(
    private val mAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) :
    IEmailVerifiedState {
    override suspend fun isEmailVerified(): Boolean {
        return if (checkInternetConnection(context)) {
            val result = withTimeoutOrNull(3000L) {
                mAuth.currentUser?.isEmailVerified
            }
            result ?: false
        } else {
            Log.d(TAG, "EmailVerifiesStateImpl : No Network")
            false
        }
    }
}