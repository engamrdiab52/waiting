package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.IUploadService
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UploadServiceImpl @Inject constructor (private val databaseReference: DatabaseReference): IUploadService {

    override suspend fun uploadService(userId: String, service: Service): Boolean {

        return try {
            databaseReference.child("services").child(userId).setValue(service).await()
            true
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            false
        }
    }
}