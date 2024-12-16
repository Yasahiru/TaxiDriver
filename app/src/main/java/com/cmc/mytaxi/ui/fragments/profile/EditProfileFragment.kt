package com.cmc.mytaxi.ui.fragments.profile

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cmc.mytaxi.App
import com.cmc.mytaxi.R
import com.cmc.mytaxi.data.local.models.Driver
import com.cmc.mytaxi.data.repository.DriverRepository
import com.cmc.mytaxi.databinding.FragmentEditProfileBinding
import com.cmc.mytaxi.data.viewmodel.ProfileViewModel
import com.cmc.mytaxi.data.viewmodel.ProfileViewModelFactory
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var driverViewModel: ProfileViewModel
    private lateinit var PersonalInfos: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("CommitTransaction")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        var edited = false
        val driverRepository = DriverRepository(App.database.driverDao())
        val factory = ProfileViewModelFactory(driverRepository)
        driverViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        driverViewModel.getDriverById(1).observe(viewLifecycleOwner) { driver ->
            driver?.let {
                displayDriverDetails(it)
            }
        }

        binding.goToPosition.setOnClickListener {
            requireActivity().finish()
        }

        binding.editProfile.setOnClickListener{
            edited = true
            val bundle = Bundle()
            val fname = binding.fname.text.toString()
            val lname = binding.lname.text.toString()
            val age = binding.age.text.toString()
            val permiType = binding.permiType.text.toString()

            bundle.putString("fname",fname)
            bundle.putString("lname",lname)
            bundle.putString("age",age)
            bundle.putString("permiType",permiType)
            bundle.putBoolean("edited",edited)

            val profileFragment = ProfileFragment()
            profileFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,profileFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    fun QRCodegenerator(infos:String){
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(infos, BarcodeFormat.QR_CODE, 400, 400)
            binding.qrcode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun displayDriverDetails(driver: Driver) {
        binding.fname.text = driver.firstName
        binding.lname.text = driver.lastName
        binding.age.text = driver.age.toString()
        binding.permiType.text = driver.permiType

        PersonalInfos = """
            Nom: ${driver.firstName}
            Prenom: ${driver.lastName}
            Age: ${driver.age}
            Type De Permie: ${driver.permiType}
        """.trimIndent()

        QRCodegenerator(PersonalInfos)
    }

}