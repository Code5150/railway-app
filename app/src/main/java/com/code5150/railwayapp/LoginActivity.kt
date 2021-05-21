package com.code5150.railwayapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.code5150.railwayapp.databinding.ActivityLoginBinding
import com.code5150.railwayapp.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginViewModel: LoginViewModel by viewModels()

        binding.loginButton.setOnClickListener {
            toggleProgressBarVisibility()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (
                        loginViewModel.login(
                            binding.editTextTextPersonName.editText!!.text.toString(),
                            binding.editTextTextPassword.editText!!.text.toString()
                        )
                    ) {
                        val intent = Intent(this@LoginActivity, SwitchChoiceActivity::class.java)
                        startActivity(intent)
                    } else {
                        withContext(Dispatchers.Main) {
                            toggleProgressBarVisibility()
                        }
                        Snackbar.make(
                            binding.layout,
                            "Неверный логин или пароль",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: SocketTimeoutException) {
                    Snackbar.make(
                        binding.layout,
                        "Время ожидания ответа вышлт",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    withContext(Dispatchers.Main) {
                        toggleProgressBarVisibility()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        toggleProgressBarVisibility()
    }

    private fun toggleProgressBarVisibility() {
            if (binding.progressBar.visibility == View.GONE) {
                binding.loginButton.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.loginButton.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
    }
}