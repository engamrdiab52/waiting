package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.data.login.IChangeOrderValue
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.waiting.MainActivity
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class ChangeOrderValueImpl @Inject constructor(private val databaseReference: DatabaseReference) :
    IChangeOrderValue {
    override suspend fun changeOrderValue(userId: String, order: Order): Boolean {
        return try {
            databaseReference.child("orders").child(userId).setValue(order).await()
            true
        } catch (e: Exception) {
            Log.d(MainActivity.TAG, e.message.toString())
            false
        }
    }
}
