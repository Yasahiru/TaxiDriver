package com.cmc.mytaxi.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cmc.mytaxi.data.local.models.Driver
import com.cmc.mytaxi.databinding.DriverItemBinding

class DriverAdapter : ListAdapter<Driver, DriverAdapter.DriverViewHolder>(DriverDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val binding = DriverItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DriverViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DriverViewHolder(private val binding: DriverItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(driver: Driver) {
            binding.fname.text = driver.firstName
            binding.lname.text = driver.lastName
            binding.age.text = driver.age.toString()
            binding.permiType.text = driver.permiType
        }
    }
}

class DriverDiffCallback : DiffUtil.ItemCallback<Driver>() {
    override fun areItemsTheSame(oldItem: Driver, newItem: Driver): Boolean {
        return oldItem.driverId == newItem.driverId
    }

    override fun areContentsTheSame(oldItem: Driver, newItem: Driver): Boolean {
        return oldItem == newItem
    }
}
