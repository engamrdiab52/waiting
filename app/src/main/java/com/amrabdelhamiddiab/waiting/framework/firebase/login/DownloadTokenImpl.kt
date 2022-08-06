package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.login.IDownloadOrder
import com.amrabdelhamiddiab.core.data.login.IDownloadToken
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DownloadTokenImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) : IDownloadToken {
    override suspend fun downloadToken(userId: String): Token? {
        return try {
            //  val userId: String? = firebaseAuth.currentUser?.uid
            val token =
                userId.let { databaseReference.child("tokens").child(it).get().await() }

          val tokenValue =   token?.getValue(Token::class.java)
            Log.d(TAG, tokenValue?.token.toString())
            tokenValue
        } catch (e: Exception) {
            Log.d(MainActivity.TAG,"ERROR............."+ e.message.toString())
            null
        }
    }
}
