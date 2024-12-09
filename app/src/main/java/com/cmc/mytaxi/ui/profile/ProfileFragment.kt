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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ProfileFragmentLayoutBinding.bind(view)

        val driverRepository = DriverRepository(App.database.driverDao())
        val factory = ProfileViewModelFactory(driverRepository)

        driverViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        driverViewModel.getDriverById(1).observe(viewLifecycleOwner) { driver ->
            driver?.let {
                displayDriverDetails(it)
            }
        }

        binding.btnAddDriver.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val permiType = binding.etPermiType.text.toString()
            val age = binding.etAge.text.toString().toInt()

            val driver = Driver(driverId = 1, firstName = firstName, lastName = lastName, age = age, permiType = permiType)
            driverViewModel.addDriver(driver)

            displayDriverDetails(driver)
        }
    }

    private fun displayDriverDetails(driver: Driver) {
        binding.idDriver.text = driver.driverId.toString()
        binding.fname.text = driver.firstName
        binding.lname.text = driver.lastName
        binding.age.text = driver.age.toString()
        binding.permiType.text = driver.permiType
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


