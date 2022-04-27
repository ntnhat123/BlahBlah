package com.codingwithme.firebasechat.`interface`


import android.view.View
import com.example.chat.Constants.Constants.Companion.CONTENT_TYPE
import com.example.chat.Constants.Constants.Companion.SERVER_KEY
import com.example.chat.model.NotificationData
import com.example.chat.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>

    fun sendNotification(notification: PushNotification): Any
}