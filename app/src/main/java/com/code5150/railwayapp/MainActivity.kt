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
import com.code5150.railwayapp.model.SwitchGroup
import com.code5150.railwayapp.network.dto.SwitchDTO
import com.code5150.railwayapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var switchAdapter: ArrayAdapter<Switch>
    private lateinit var switchGroupAdapter: ArrayAdapter<SwitchGroup>

    private lateinit var switch: Switch
    private lateinit var switchGroup: SwitchGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val viewModel: MainViewModel by viewModels()

        viewModel.staff.observe(this, {
            title = it.toString()
        })

        viewModel.switchGroups.observe(this, {
            switchGroupAdapter =
                ArrayAdapter(this@MainActivity, R.layout.dropdown_item, it).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    adapter.filter.filter(null)
                    binding.switchGroupDropdown.setAdapter(adapter)
                    binding.switchGroupDropdown.setText(adapter.getItem(0).toString(), false)
                    switchGroup = adapter.getItem(0)!!
                }
        })

        viewModel.switches.observe(this, {
            switchAdapter = ArrayAdapter(this@MainActivity, R.layout.dropdown_item, it)
            switchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            switchAdapter.filter.filter(null)
            binding.switchDropdown.setAdapter(switchAdapter)
            binding.switchDropdown.setText(switchAdapter.getItem(0).toString(), false)
            switch = switchAdapter.getItem(0)!!
        })

        binding.switchDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val item = switchAdapter.getItem(position)!!
                Toast.makeText(
                    this@MainActivity,
                    "Id: ${item.id}\nName: ${item.name}\nGroup: ${item.switchGroupId}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}