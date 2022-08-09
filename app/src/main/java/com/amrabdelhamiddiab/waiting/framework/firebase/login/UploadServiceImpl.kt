package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.IUploadService
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UploadServiceImpl @Inject constructor(
    private val mAuth: FirebaseAuth,
    @ApplicationContext private val context: Context,
    private val databaseReference: DatabaseReference
) : IUploadService {

    override suspend fun uploadService(service: Service): Boolean {
        return if (checkInternetConnection(context)) {
            try {
                val uid = mAuth.currentUser?.uid ?: ""
                if (uid.isNotEmpty()) {
                    databaseReference.child("services").child(uid).setValue(service).await()
                    databaseReference.child("orders").child(uid).setValue(Order(0L)).await()
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