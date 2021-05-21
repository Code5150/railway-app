package com.code5150.railwayapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.code5150.railwayapp.databinding.ActivitySwitchChoiceBinding
import com.code5150.railwayapp.model.Switch
import com.code5150.railwayapp.model.SwitchGroup
import com.code5150.railwayapp.viewmodel.SwitchChoiceViewModel

class SwitchChoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySwitchChoiceBinding
    private lateinit var switchAdapter: ArrayAdapter<Switch>
    private lateinit var switchGroupAdapter: ArrayAdapter<SwitchGroup>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwitchChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val viewModel: SwitchChoiceViewModel by viewModels()

        viewModel.staff.observe(this, {
            title = it.toString()
        })

        if (viewModel.switchGroups.value == null) {
            makeInvisible(binding.switchGroupList)
            makeVisible(binding.switchGroupProgressBar)
        }

        if (viewModel.switches.value == null) {
            makeInvisible(binding.switchList)
            makeVisible(binding.switchProgressBar)
        }

        viewModel.switchGroups.observe(this, {
            switchGroupAdapter = createAdapter(it, binding.switchGroupDropdown).also { adapter ->
                viewModel.switchGroup.value = adapter.getItem(0)!!
                makeVisible(binding.switchGroupList)
                makeInvisible(binding.switchGroupProgressBar)
            }
        })

        viewModel.switches.observe(this, {
            switchAdapter = createAdapter(it, binding.switchDropdown).also { adapter ->
                viewModel.switch.value = adapter.getItem(0)!!
                makeVisible(binding.switchList)
                makeInvisible(binding.switchProgressBar)
            }
        })

        binding.switchDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val item = switchAdapter.getItem(position)!!
                Toast.makeText(
                    this@SwitchChoiceActivity,
                    "Id: ${item.id}\nName: ${item.name}\nGroup: ${item.switchGroupId}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        binding.switchGroupDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val item = switchGroupAdapter.getItem(position)!!
                Toast.makeText(
                    this@SwitchChoiceActivity,
                    "Id: ${item.id}\nName: ${item.name}",
                    Toast.LENGTH_SHORT
                ).show()

                makeInvisible(binding.switchList)
                makeVisible(binding.switchProgressBar)

                viewModel.filterSwitchesByGroup(item.id)
            }

        binding.takeMeasuresButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }

    private inline fun <T> createAdapter(
        items: List<T>,
        dropdown: AutoCompleteTextView
    ): ArrayAdapter<T> =
        ArrayAdapter(this@SwitchChoiceActivity, R.layout.dropdown_item, items).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter.filter.filter(null)
            dropdown.setAdapter(adapter)
            dropdown.setText(adapter.getItem(0).toString(), false)
        }

    private inline fun<T: View> makeVisible(view: T) {
        view.visibility = View.VISIBLE
    }

    private inline fun<T: View> makeInvisible(view: T) {
        view.visibility = View.INVISIBLE
    }

    private inline fun<T: View> makeGone(view: T) {
        view.visibility = View.GONE
    }
}