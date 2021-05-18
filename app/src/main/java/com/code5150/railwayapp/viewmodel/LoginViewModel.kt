package com.code5150.railwayapp.viewmodel

import androidx.lifecycle.ViewModel
import com.code5150.railwayapp.network.ApiInterface
import java.security.MessageDigest

class LoginViewModel: ViewModel() {

    private val apiService = ApiInterface()

    suspend fun login(login: String, password: String): Boolean {
        val passwordHash = MessageDigest.getInstance("SHA-512")
            .digest(password.toByteArray())
            .fold("", {str, b -> str + "%02x".format(b)})

        return apiService.authorize(login, password)
    }
}