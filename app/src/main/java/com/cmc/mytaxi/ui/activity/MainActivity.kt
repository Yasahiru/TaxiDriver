package com.cmc.mytaxi.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.cmc.mytaxi.App
import com.cmc.mytaxi.R
import com.cmc.mytaxi.data.repository.DriverRepository
import com.cmc.mytaxi.data.viewmodel.ProfileViewModel
import com.cmc.mytaxi.data.viewmodel.ProfileViewModelFactory
import com.cmc.mytaxi.ui.fragments.profile.EditProfileFragment

import com.cmc.mytaxi.ui.fragments.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var driverViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val driverRepository = DriverRepository(App.database.driverDao())
        val factory = ProfileViewModelFactory(driverRepository)
        driverViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        val targetFragment = intent.getStringExtra("MainActivity")

        if (targetFragment != null) {
            when (targetFragment) {
                "editProfile" -> {
                    val fragment = EditProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, fragment)
                        .commit()
                }
                "Profile" -> {
                    val fragment = EditProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, fragment)
                        .commit()
                }
                else -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, ProfileFragment())
                        .commit()
                }
            }
        } else {
            driverViewModel.getDriverById(1).observe(this) { driver ->
                if (driver?.isCreated == true) {
                    val intent = Intent(this, HomePage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, ProfileFragment())
                        .commit()
                }
            }
        }

    }

}