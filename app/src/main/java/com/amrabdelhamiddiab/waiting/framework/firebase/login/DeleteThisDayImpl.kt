package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.login.IDeleteService
import com.amrabdelhamiddiab.core.data.login.IDeleteThisDay
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteThisDayImpl @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference,
    @ApplicationContext private val context: Context
) : IDeleteThisDay {
    override suspend fun deleteThisDay(): Boolean {
        return if (checkInternetConnection(context)) {
            try {
                val uid = mAuth.currentUser?.uid ?: ""
                if (uid.isNotEmpty()) {
                    databaseReference.child("orders").child(uid).removeValue().await()
                    databaseReference.child("tokens").child(uid).removeValue().await()
                    true
                } else {
                    false
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
            false
        }

    }
}