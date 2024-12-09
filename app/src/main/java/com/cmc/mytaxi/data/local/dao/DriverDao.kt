package com.cmc.mytaxi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cmc.mytaxi.data.local.models.Driver
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDriver(driver : Driver)

    @Query("Select * from drivers")
    fun getAllDrivers() : Flow<List<Driver>>

    @Query("SELECT * FROM drivers WHERE driverId = :driverId")
    suspend fun getDriverById(driverId: Int): Driver?

}