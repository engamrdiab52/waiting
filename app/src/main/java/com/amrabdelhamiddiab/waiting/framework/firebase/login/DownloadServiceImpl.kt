package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.IDownloadService
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DownloadServiceImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) : IDownloadService {
    override suspend fun downloadService(userId: String): Service? {
        return try {
          //  val userId: String? = firebaseAuth.currentUser?.uid
            val service =
                userId.let { databaseReference.child("services").child(it).get().await() }
            service?.getValue(Service::class.java)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            null
        }
    }
}
