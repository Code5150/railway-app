package com.code5150.railwayapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.code5150.railwayapp.databinding.ActivityLoginBinding
import com.code5150.railwayapp.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginViewModel: LoginViewModel by viewModels()

        binding.loginButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (loginViewModel.login(
                        binding.editTextTextPersonName.text.toString(),
                        binding.editTextTextPassword.text.toString()
                    )
                ) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}