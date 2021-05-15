package com.code5150.railwayapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.code5150.railwayapp.model.Staff
import com.code5150.railwayapp.network.ApiInterface
import com.code5150.railwayapp.network.dto.SwitchDTO

class MainViewModel: ViewModel() {

    private val apiService = ApiInterface()

    var switches = liveData {
        emit(apiService.getAllSwitches())
    }
        private set

    val staff = liveData {

        emit(Staff(apiService.getCurrentUser()))
    }


}