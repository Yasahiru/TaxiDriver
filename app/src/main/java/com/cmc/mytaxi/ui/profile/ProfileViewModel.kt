package com.cmc.mytaxi.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.mytaxi.data.local.models.Driver
import com.cmc.mytaxi.data.repository.DriverRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val driverRepository: DriverRepository) : ViewModel() {

    fun addDriver(driver: Driver) {
        viewModelScope.launch {
            driverRepository.upsertDriver(driver)
        }
    }
}
