package com.cmc.mytaxi.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.cmc.mytaxi.R
import com.cmc.mytaxi.data.viewmodel.CalculatTraficViewModel
import com.cmc.mytaxi.data.viewmodel.CalculatTraficViewModelFactory
import com.cmc.mytaxi.databinding.ActivityHomePageBinding
import com.cmc.mytaxi.ui.fragments.profile.ProfileFragment
import com.cmc.mytaxi.utils.NotificationHelper
import com.cmc.mytaxi.utils.PermissionsHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pub.devrel.easypermissions.EasyPermissions
import java.util.Locale

class HomePage : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var viewModel: CalculatTraficViewModel
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isRideActive: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 5000

    private companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val factory = CalculatTraficViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[CalculatTraficViewModel::class.java]
        notificationHelper = NotificationHelper(this)

        setupLocationServices()
        setupRideToggleButton()
        observeRideData()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupMap()

        //end oncreate
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile->{
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("MainActivity", "Profile")
                }
                startActivity(intent)
                true
            }
            R.id.lang -> {
                Toast.makeText(this,"Language mode",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.light->{
                Toast.makeText(this,"light mode",Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setTheme(){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_theme, null)
        builder.setView(dialogView)
        builder.setTitle("Custom Dialog")

        builder.setPositiveButton("set"){dialog,_->
            Toast.makeText(this,"test",Toast.LENGTH_SHORT).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun language(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        createConfigurationContext(config)
    }

    fun setlang(){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_layout, null)
        builder.setView(dialogView)

        builder.setTitle("Custom Dialog")
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun setupLocationServices() {
        PermissionsHelper.checkAndPromptLocationServices(
            this,
            onLocationSettingsSatisfied = {
                Toast.makeText(this, "Location services are already enabled.", Toast.LENGTH_SHORT).show()
            },
            onResolutionRequired = { exception ->
                try {
                    exception.startResolutionForResult(
                        this,
                        PermissionsHelper.LOCATION_REQUEST_CODE
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Toast.makeText(
                        this,
                        "Unable to start resolution: ${sendEx.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onFailure = { exception ->
                Toast.makeText(
                    this,
                    "Failed to check location settings: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun setupRideToggleButton() {
        binding.btnToggleRide.setOnClickListener {
            if (!PermissionsHelper.hasLocationPermission(this)) {
                PermissionsHelper.requestLocationPermission(this)
                return@setOnClickListener
            }

            if (!isRideActive) {
                startRide()
                binding.btnToggleRide.text = getString(R.string.end_ride)
                isRideActive = true
            } else {
                endRide()
                binding.btnToggleRide.text = getString(R.string.start_ride)
                isRideActive = false
            }
        }
    }

    private fun observeRideData() {
        viewModel.rideData.observe(this) { ride ->
            val distance = String.format(Locale.ROOT, "%.2f", ride.distance)
            val fare = String.format(Locale.ROOT, "%.2f", ride.totalFare)

            binding.tvDistance.text = getString(R.string.distance_format, distance)
            binding.tvTimeElapsed.text = getString(R.string.time_elapsed_format, ride.timeElapsed)
            binding.tvTotalFare.text = getString(R.string.total_fare_format, fare)
        }
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableLocation()
    }

    private fun enableLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingPermission")
    @Suppress("DEPRECATION")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let { updateMapLocation(it) }
        }

        fusedLocationClient.requestLocationUpdates(
            com.google.android.gms.location.LocationRequest.create().apply {

                interval = 5000
                fastestInterval = 2000
                priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
            },
            object : com.google.android.gms.location.LocationCallback() {
                override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                    locationResult.lastLocation?.let { updateMapLocation(it) }
                }
            },
            null
        )
    }

    private fun updateMapLocation(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        map.clear()
        map.addMarker(MarkerOptions().position(currentLatLng).title("Ma position actuelle"))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
    }

    private fun startRide() {
        viewModel.startRide()
        handler.postDelayed(updateRideTask, updateInterval)
    }

    private fun endRide() {
        handler.removeCallbacks(updateRideTask)
        viewModel.rideData.value?.let { ride ->
            notificationHelper.sendFareNotification(
                ride.totalFare, ride.distance, ride.timeElapsed
            )
        }
    }

    private val updateRideTask = object : Runnable {
        override fun run() {
            if (isRideActive) {
                viewModel.updateLocation()
                handler.postDelayed(this, updateInterval)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation()
            }
        }

        when {
            EasyPermissions.somePermissionPermanentlyDenied(this, permissions.toList()) -> {
                PermissionsHelper.showSettingsDialog(this)
            }
            PermissionsHelper.hasLocationPermission(this) -> {
                Toast.makeText(
                    this,
                    "Permission granted. You can now start the ride.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                Toast.makeText(this, "Permission denied. Cannot start the ride.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PermissionsHelper.LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Location services enabled!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location services not enabled.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_page_menu, menu)
        return true
    }






}