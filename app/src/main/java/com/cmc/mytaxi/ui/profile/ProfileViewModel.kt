package com.cmc.mytaxi.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.mytaxi.data.local.models.Driver
import com.cmc.mytaxi.data.repository.DriverRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(private val driverRepository: DriverRepository) : ViewModel() {

    val allDrivers:StateFlow<List<Driver>> = driverRepository.getAllDrivers()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addDriver(driver: Driver) {
        viewModelScope.launch {
            driverRepository.upsertDriver(driver)
        }
    }

    fun getAllDrivers() {
        viewModelScope.launch {
            driverRepository.getAllDrivers()
        }
    }


}