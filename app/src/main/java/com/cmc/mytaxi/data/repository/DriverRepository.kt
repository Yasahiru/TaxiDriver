package com.cmc.mytaxi.data.repository

import com.cmc.mytaxi.data.local.dao.DriverDao
import com.cmc.mytaxi.data.local.models.Driver
import kotlinx.coroutines.flow.Flow

class DriverRepository(private val driverDao: DriverDao) {

    fun getAllDrivers(): Flow<List<Driver>> = driverDao.getAllDrivers()

    suspend fun upsertDriver(driver: Driver) {
        driverDao.upsertDriver(driver)
    }

    suspend fun getDriverById(id: Int): Driver? {
        return driverDao.getDriverById(id)
    }
}
