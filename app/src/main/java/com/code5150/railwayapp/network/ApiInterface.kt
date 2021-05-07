package com.code5150.railwayapp.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST("auth")
    suspend fun authorize(
        @Field("username") username: String,
        @Field("password") password: String
    )

    companion object {
        private const val API_URL = "http://localhost:8080/api/v1/"
        operator fun invoke(): ApiInterface {
            val gson = GsonBuilder().create()
            return Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(
                    OkHttpClient().newBuilder()
                        //.cookieJar()
                        .build()
                )
                .build()
                .create(ApiInterface::class.java)
        }
    }
}