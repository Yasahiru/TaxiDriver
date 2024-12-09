package com.cmc.mytaxi.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cmc.mytaxi.App
import com.cmc.mytaxi.R
import com.cmc.mytaxi.data.local.models.Driver
import com.cmc.mytaxi.data.repository.DriverRepository
import com.cmc.mytaxi.databinding.ProfileFragmentLayoutBinding

class ProfileFragment : Fragment(R.layout.profile_fragment_layout) {

    private var _binding: ProfileFragmentLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var driverViewModel: ProfileViewModel
    private lateinit var driver: Driver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ProfileFragmentLayoutBinding.bind(view)

        val driverRepository = DriverRepository(App.database.driverDao())
        val factory = ProfileViewModelFactory(driverRepository)

        driverViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        binding.btnAddDriver.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val permiType = binding.etPermiType.text.toString()
            val age = binding.etAge.text.toString().toInt()

            driverViewModel.addDriver(Driver(id, firstName, lastName, age, permiType))

            binding.idDriver.text = driver.driverId.toString()
            binding.fname.text = driver.firstName
            binding.lname.text = driver.lastName
            binding.age.text = driver.age.toString()
            binding.permiType.text = driver.permiType

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

