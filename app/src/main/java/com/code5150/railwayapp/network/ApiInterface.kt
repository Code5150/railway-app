package com.code5150.railwayapp.network

import com.code5150.railwayapp.network.dto.StaffDTO
import com.code5150.railwayapp.network.dto.SwitchDTO
import com.code5150.railwayapp.network.dto.SwitchGroupDTO
import com.google.gson.GsonBuilder
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST(AUTH)
    suspend fun authorize(
        @Field("username") username: String,
        @Field("password") password: String
    ): Boolean

    @GET("allSwitches")
    suspend fun getAllSwitches(): List<SwitchDTO>
    @GET("allSwitchGroups")
    suspend fun getAllSwitchGroups(): List<SwitchGroupDTO>
    @GET("currentUser")
    suspend fun getCurrentUser(): StaffDTO
    @GET("switchesInGroup/{groupId}")
    suspend fun getSwitchesByGroup(@Path("groupId") groupId: Int): List<SwitchDTO>

    companion object {
        private const val API_URL = "https://railwayserver.herokuapp.com/api/v1/"
        private const val AUTH = "auth"

        private val client = OkHttpClient().newBuilder()
            .cookieJar(SessionCookieJar())
            .build()
        private val interfaceInstance = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiInterface::class.java)

        operator fun invoke(): ApiInterface = interfaceInstance

        private class SessionCookieJar: CookieJar{
            private var cookies: MutableList<Cookie>? = null

            override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
                if (url.encodedPath().endsWith(AUTH)){
                    this.cookies = cookies
                }
            }

            override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                cookies?.let {
                    if (!url.encodedPath().endsWith(AUTH)) return it
                }
                return emptyList<Cookie>().toMutableList()
            }

        }
    }
}