package com.amrabdelhamiddiab.waiting.framework.firebase.login

import android.util.Log
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class NotifyOrderChangerImpl @Inject constructor() : ValueEventListener {

    override fun onDataChange(snapshot: DataSnapshot) {
      //  Log.d(TAG, snapshot.value.toString())
        val item = snapshot.getValue(Order::class.java)
        Log.d(TAG, item.toString())
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d(TAG, error.message)
    }
}