package com.code5150.railwayapp.viewmodel

import androidx.lifecycle.*
import com.code5150.railwayapp.model.Staff
import com.code5150.railwayapp.model.Switch
import com.code5150.railwayapp.model.SwitchGroup
import com.code5150.railwayapp.network.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SwitchChoiceViewModel: ViewModel() {

    private val apiService = ApiInterface()

    lateinit var switch: MutableLiveData<Switch>
        private set

    lateinit var switchGroup: MutableLiveData<SwitchGroup>
        private set

    private val _switches = liveData {
        emit(apiService.getAllSwitches().map { Switch(it) })
    } as MutableLiveData

    val switches = _switches as LiveData<List<Switch>>

    private val _switchGroups = liveData {
        emit(apiService.getAllSwitchGroups().map { SwitchGroup(it) })
    } as MutableLiveData

    val switchGroups = _switchGroups as LiveData<List<SwitchGroup>>

    val staff = liveData {
        emit(Staff(apiService.getCurrentUser()))
    }

    fun filterSwitchesByGroup(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _switches.value = apiService.getSwitchesByGroup(id).map { Switch(it) }
        }
    }
}