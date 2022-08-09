package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.login.IDeleteAccount
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteAccountImpl @Inject constructor(
    private val mAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : IDeleteAccount {
    override suspend fun deleteAccount(password: String): Boolean {
        if (checkInternetConnection(context)) {
            return try {
                if (mAuth.currentUser != null) {
                    val credentials = EmailAuthProvider.getCredential(
                        mAuth.currentUser!!.email.toString(),
                        password
                    )
                    mAuth.currentUser!!.reauthenticate(credentials).await()
                    mAuth.currentUser!!.delete().await()
                    Log.d(TAG, mAuth.currentUser.toString())
                    mAuth.currentUser == null
                } else {
                    true
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
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