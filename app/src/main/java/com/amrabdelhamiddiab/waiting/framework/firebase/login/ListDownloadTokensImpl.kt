package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.login.IListDownloadTokens
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListDownloadTokensImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : IListDownloadTokens {
    private val _listOfTokens: MutableList<Token> = mutableListOf()
    private val listOfTokens: List<Token> = _listOfTokens

    override suspend fun listDownloadTokens(): List<Token>? {
        if (checkInternetConnection(context)) {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            return if (uid.isNotEmpty()) {
                try {
                    _listOfTokens.clear()
                    val snapshot = databaseReference.child("tokens").child(uid).get().await()
                    snapshot.children.forEach {
                        val token = it.getValue(Token::class.java)
                        if (token != null) {
                            _listOfTokens.add(token)
                        }
                    }
                    listOfTokens
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                    null
                }
            } else {
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