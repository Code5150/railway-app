package com.code5150.railwayapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.code5150.railwayapp.databinding.ActivityLoginBinding
import com.code5150.railwayapp.databinding.ActivityMainBinding
import com.code5150.railwayapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: MainViewModel by viewModels()

        viewModel.staff.observe(this, {
            title = it.toString()
        })
    }
}