package com.code5150.railwayapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.code5150.railwayapp.databinding.ActivityLoginBinding
import com.code5150.railwayapp.databinding.ActivityMainBinding
import com.code5150.railwayapp.model.Switch
import com.code5150.railwayapp.network.dto.SwitchDTO
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

        viewModel.switches.observe(this, {
            ArrayAdapter(this, android.R.layout.simple_spinner_item, it).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
            }
        })

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = parent!!.selectedItem as Switch
                Toast.makeText(
                        this@MainActivity,
                        "Id: ${item.id}\nName: ${item.name}\nGroup: ${item.switchGroupId}",
                        Toast.LENGTH_SHORT
                    ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(
                    this@MainActivity,
                    "Nothing selected",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}