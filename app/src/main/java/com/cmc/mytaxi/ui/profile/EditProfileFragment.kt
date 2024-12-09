package com.cmc.mytaxi.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.cmc.mytaxi.App
import com.cmc.mytaxi.R
import com.cmc.mytaxi.data.local.models.Driver
import com.cmc.mytaxi.data.repository.DriverRepository
import com.cmc.mytaxi.databinding.FragmentEditProfileBinding
import com.cmc.mytaxi.databinding.ProfileFragmentLayoutBinding


class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var driverViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        val driverRepository = DriverRepository(App.database.driverDao())
        val factory = ProfileViewModelFactory(driverRepository)
        driverViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        driverViewModel.getDriverById(1).observe(viewLifecycleOwner) { driver ->
            driver?.let {
                displayDriverDetails(it)
            }
        }

    }

    private fun displayDriverDetails(driver: Driver) {
        binding.idDriver.text = driver.driverId.toString()
        binding.fname.text = driver.firstName
        binding.lname.text = driver.lastName
        binding.age.text = driver.age.toString()
        binding.permiType.text = driver.permiType
    }


}