package com.amrabdelhamiddiab.waiting.framework.firebase.fcm

import com.amrabdelhamiddiab.core.domain.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val SERVER_KEY =
    "AAAAaqkq-wA:APA91bFCGQsMPuw9p80c_wbpRxT8Ma_er06LwI9-sQ5DOzA1iTqS0J5v3PrLp3t43MVJeFwuG8wXiAhTixCy3d6HQ8-LecF43gBe2gittk0RwNXPhHhhX4UuopQbStzURzxcvQR5SAL3"
private const val CONTENT_TYPE = "application/json"

interface FcmService {
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<ResponseBody>


    companion object {
        private const val BASE_URL = "https://fcm.googleapis.com"

        fun create(): FcmService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(FcmService::class.java)
        }

    }


}