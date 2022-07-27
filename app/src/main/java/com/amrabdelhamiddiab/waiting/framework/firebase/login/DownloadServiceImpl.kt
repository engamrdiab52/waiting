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
    override suspend fun downloadService(): Service? {
        return try {
            val userId: String? = firebaseAuth.currentUser?.uid
            val service =
                userId?.let { databaseReference.child("services").child(it).get().await() }
            service?.getValue(Service::class.java)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            Service("", "", "", 0)
        }
    }
}

/*     return try {
              val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
              val snapshot = userId?.let { databaseReference.child("services").child(it).get().await() }
              snapshot?.children?.forEach {
                  val item = it.getValue(Service::class.java)
                  item?.let { it1 -> _listOfFavorites.add(it1) }
              }
              listOfFavorites

          } catch (e: Exception) {
              Log.d(TAG, "DownloadFavoritesImpl  :  " + e.message.toString())
              emptyList()
          }*/

/* userId?.let {
       databaseReference.child("services").child(it).get().addOnSuccessListener {
           Log.d(TAG, it.value.toString())
       }.addOnFailureListener {
           Log.d(TAG, it.message.toString())
       }
   }*/