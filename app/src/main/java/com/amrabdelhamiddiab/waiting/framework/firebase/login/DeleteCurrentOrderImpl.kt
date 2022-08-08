package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.login.IDeleteCurrentOrder
import com.amrabdelhamiddiab.waiting.MainActivity
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DeleteCurrentOrderImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    @ApplicationContext private val context: Context
) :
    IDeleteCurrentOrder {
    override suspend fun deleteCurrentOrder(userId: String) {
        try {
            databaseReference.child("orders").child(userId).removeValue().await()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }
}