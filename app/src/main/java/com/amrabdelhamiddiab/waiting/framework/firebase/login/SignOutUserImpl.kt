package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.login.ISignOutUser
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignOutUserImpl @Inject constructor(
    private val mAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ISignOutUser {
    override suspend fun signOutUser(): Boolean {
        return if (checkInternetConnection(context)) {
            try {
                mAuth.signOut()
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
            false
        }

    }
}