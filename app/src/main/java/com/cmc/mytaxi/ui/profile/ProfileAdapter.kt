package com.cmc.mytaxi.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cmc.mytaxi.data.local.models.Driver
import com.cmc.mytaxi.databinding.DriverItemBinding

class ProfileAdapter(val driverList:List<Driver>):RecyclerView.Adapter<ProfileAdapter.DriverViewHolder>() {

    inner class DriverViewHolder(private val binding: DriverItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(driver: Driver) {
            binding.idDriver.text = driver.driverId.toString()
            binding.fname.text = driver.firstName
            binding.lname.text = driver.lastName
            binding.age.text = driver.age.toString()
            binding.permiType.text = driver.permiType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val binding = DriverItemBinding.inflate(LayoutInflater.from(parent.context),parent,false )
        return DriverViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = driverList[position]
        holder.bind(driver)
    }

    override fun getItemCount() = driverList.size

}