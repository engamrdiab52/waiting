package com.amrabdelhamiddiab.waiting.framework.firebase.fcm

import com.amrabdelhamiddiab.core.domain.PushNotification
import com.amrabdelhamiddiab.waiting.framework.utilis.Constants.Companion.CONTENT_TYPE
import com.amrabdelhamiddiab.waiting.framework.utilis.Constants.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FcmService {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<ResponseBody>


    companion object {
      private  const val BASE_URL = "https://fcm.googleapis.com"

        fun create(): FcmService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(FcmService::class.java)
        }

    }


}