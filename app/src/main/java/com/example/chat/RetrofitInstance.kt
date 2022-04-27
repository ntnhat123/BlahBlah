package com.example.chat

import com.codingwithme.firebasechat.`interface`.NotificationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{
        private val BASE_URL = "https://fcm.googleapis.com/"
        private var retrofit: Retrofit? = null

        fun getRetrofitInstance(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
        val api by lazy {
            getRetrofitInstance()!!.create(NotificationApi::class.java)
        }
    }
}