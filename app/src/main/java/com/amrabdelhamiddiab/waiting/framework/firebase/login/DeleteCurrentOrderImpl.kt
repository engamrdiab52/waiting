package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.login.IDeleteCurrentOrder
import com.amrabdelhamiddiab.waiting.MainActivity
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DeleteCurrentOrderImpl @Inject constructor(private val databaseReference: DatabaseReference) :
    IDeleteCurrentOrder {
    override suspend fun deleteCurrentOrder(userId: String) {
        try {
            databaseReference.child("orders").child(userId).removeValue().await()
        }catch (e: Exception){
            Log.d(MainActivity.TAG, e.message.toString())

        }
    }
}