package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amrabdelhamiddiab.core.data.login.IChangeOrderValue
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class ChangeOrderValueImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    @ApplicationContext private val context: Context
) :
    IChangeOrderValue {
    override suspend fun changeOrderValue(userId: String, order: Order): Boolean {
        return if (checkInternetConnection(context)) {
            try {
                databaseReference.child("orders").child(userId).setValue(order).await()
                true
            } catch (e: Exception) {
                Log.d(MainActivity.TAG, e.message.toString())
                false
            }
        } else {
            Toast.makeText(context, "please check your internet connection", Toast.LENGTH_LONG).show()
            false
        }


    }
}