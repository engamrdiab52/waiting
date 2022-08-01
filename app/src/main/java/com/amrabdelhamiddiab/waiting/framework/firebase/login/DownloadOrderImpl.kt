package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.login.IDownloadOrder
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.waiting.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DownloadOrderImpl@Inject constructor(
    private val databaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) : IDownloadOrder {
    override suspend fun downloadOrder(userId: String): Order? {
        return try {
            //  val userId: String? = firebaseAuth.currentUser?.uid
            val order =
                userId.let { databaseReference.child("orders").child(it).get().await() }
            order?.getValue(Order::class.java)
        } catch (e: Exception) {
            Log.d(MainActivity.TAG, e.message.toString())
            null
        }
    }
}
