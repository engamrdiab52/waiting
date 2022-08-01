package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.login.IDeleteService
import com.amrabdelhamiddiab.waiting.MainActivity
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DeleteServiceImpl @Inject constructor(private val databaseReference: DatabaseReference) : IDeleteService {
    override suspend fun deleteService(userId: String) {
        try {
            databaseReference.child("services").child(userId).removeValue().await()
        }catch (e: Exception){
            Log.d(MainActivity.TAG, e.message.toString())

        }
    }
}