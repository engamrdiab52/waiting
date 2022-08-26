package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.login.IDownloadToken
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadTokenImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : IDownloadToken {
    override suspend fun downloadToken(): Token? {
        if (checkInternetConnection(context)) {
            return try {
                val uid = firebaseAuth.currentUser?.uid ?: ""
                if (uid.isNotEmpty()) {
                    val token =
                        databaseReference.child("tokens").child(uid)
                            .get().await()
                    val tokenValue = token?.getValue(Token::class.java)
                    Log.d(TAG, tokenValue?.token.toString())
                    tokenValue
                } else {
                    null
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
                null
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "PLEASE CHECK INTERNET CONNECTION", Toast.LENGTH_LONG)
                    .show()
            }
            return null
        }

    }
}
