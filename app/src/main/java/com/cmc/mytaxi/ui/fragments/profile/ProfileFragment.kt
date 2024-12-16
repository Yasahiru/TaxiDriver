package com.cmc.mytaxi.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cmc.mytaxi.App
import com.cmc.mytaxi.R
import com.cmc.mytaxi.data.local.models.Driver
import com.cmc.mytaxi.data.repository.DriverRepository
import com.cmc.mytaxi.databinding.ProfileFragmentLayoutBinding
import com.cmc.mytaxi.data.viewmodel.ProfileViewModel
import com.cmc.mytaxi.data.viewmodel.ProfileViewModelFactory
import com.cmc.mytaxi.ui.activity.HomePage

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

        val edited = arguments?.getBoolean("edited")

        if(edited == true){
            val name = arguments?.getString("fname")
            val lname = arguments?.getString("lname")
            val aage = arguments?.getString("age")
            val permitype = arguments?.getString("permiType")

            binding.etFirstName.setText(name)
            binding.etLastName.setText(lname)
            binding.etPermiType.setText(permitype)
            binding.etAge.setText(aage)
        }

        binding.btnAddDriver.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val permiType = binding.etPermiType.text.toString()
            val age = binding.etAge.text.toString()

            if(firstName!= "" && lastName!="" && permiType!="" && age!=""){
                val driver = Driver(driverId = 1, firstName = firstName, lastName = lastName, age = age.toInt(), permiType = permiType ,isCreated = true)
                driverViewModel.addDriver(driver)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, EditProfileFragment())
                    .commit()
                
            }else{
                Toast.makeText(requireContext(),"Please Fill The Fields",Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}