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
    "AAAAxyZDfww:APA91bHj8Y2UI32deYrTVfUDCAFaWn2RLVYasEM9LtyN5xdULugOkHCHbnf8Egs3vvCjgmtvCXGBmlqlw6l3je53S_1Ba93dhQMHPzxacBlfcPz4-RMKN6-hw9J9ot1ENDUKm7mtxkfy"
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