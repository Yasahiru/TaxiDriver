package com.cmc.mytaxi.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.mytaxi.App
import com.cmc.mytaxi.R
import com.cmc.mytaxi.data.local.models.Driver
import com.cmc.mytaxi.data.repository.DriverRepository
import com.cmc.mytaxi.databinding.ProfileFragmentLayoutBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.profile_fragment_layout) {

    private var _binding: ProfileFragmentLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var driverViewModel: ProfileViewModel
    private lateinit var driverAdapter: DriverAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ProfileFragmentLayoutBinding.bind(view)

        val driverRepository = DriverRepository(App.database.driverDao())
        val factory = ProfileViewModelFactory(driverRepository)

        driverViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        driverAdapter = DriverAdapter()
        binding.rv.adapter = driverAdapter

        lifecycleScope.launch {
            driverViewModel.allDrivers.collect { drivers ->
                driverAdapter.submitList(drivers)
            }
        }

        binding.btnAddDriver.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val permiType = binding.etPermiType.text.toString()
            val age = binding.etAge.text.toString().toInt()

            driverViewModel.addDriver(Driver(id, firstName, lastName, age, permiType))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

