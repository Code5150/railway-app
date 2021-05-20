package com.code5150.railwayapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.code5150.railwayapp.model.Staff
import com.code5150.railwayapp.model.Switch
import com.code5150.railwayapp.model.SwitchGroup
import com.code5150.railwayapp.network.ApiInterface
import com.code5150.railwayapp.network.dto.SwitchDTO

class MainViewModel: ViewModel() {

    private val apiService = ApiInterface()

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
}