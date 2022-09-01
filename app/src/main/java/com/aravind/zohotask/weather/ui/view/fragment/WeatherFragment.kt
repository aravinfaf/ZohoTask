package com.aravind.zohotask.weather.ui.view.fragment

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.aravind.zohotask.R
import com.aravind.zohotask.databinding.ActivityWeatherBinding
import com.aravind.zohotask.network.model.WeatherModel
import com.aravind.zohotask.util.*
import com.aravind.zohotask.weather.ui.viewmodel.WeatherViewmodel
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var binding: ActivityWeatherBinding? = null
    private val viewmodel: WeatherViewmodel by viewModels()
    private val PERMISSION_REQUEST_CODE = 200
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Constants.SCREEN = "2"
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.weather)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewmodel.weatherLiveData.observe(viewLifecycleOwner, weatherObserver)

        getLocationUpdates()
    }

    private val weatherObserver = Observer<Resource<WeatherModel>> {
        when (it.status) {
            Status.LOADING -> {
                binding?.progressBar?.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
                binding?.progressBar?.visibility = View.GONE
                it.data?.let { weatherModel ->
                    val iconCode = weatherModel.weather.first().icon.replace("n", "d")
                    changeBgAccToTemp(iconCode)
                    binding?.apply {
                        AppUtils.setGlideImageview(imageWeatherSymbol, Constants.WEATHER_API_IMAGE
                                + "${iconCode}@4x.png")

                        textTodaysDate.text = AppUtils.getCurrentDateTime(Constants.DATE_FORMAT)

                        textTemperature.text = weatherModel.main.temp.toString()

                        binding?.textCityName?.text =
                            weatherModel.name + ", " + weatherModel.sys.country
                    }
                }
            }
            Status.ERROR -> {
                binding?.progressBar?.visibility = View.GONE
                requireActivity().showToast(it?.message!!)
                binding?.noDataImg?.visibility = View.VISIBLE
            }
        }
    }

    private fun changeBgAccToTemp(iconCode: String?) {
        binding?.constraintLayoutShowingTemp?.visibility = View.VISIBLE
        when (iconCode) {
            "01d", "02d", "03d" -> binding?.imageWeatherHumanReaction?.setImageResource(R.drawable.sunny_day)
            "04d", "09d", "10d", "11d" -> binding?.imageWeatherHumanReaction?.setImageResource(R.drawable.raining)
            "13d", "50d" -> binding?.imageWeatherHumanReaction?.setImageResource(R.drawable.snowfalling)
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val locationAccepted = grantResults[0] === PackageManager.PERMISSION_GRANTED
                if (locationAccepted) Snackbar.make(binding?.root!!,
                    "Permission Granted, Now you can access location data and camera.",
                    Snackbar.LENGTH_LONG).show() else {
                    Snackbar.make(binding?.root!!,
                        "Permission Denied, You cannot access location data and camera.",
                        Snackbar.LENGTH_LONG).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access the permissions"
                            ) { _, _ ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(arrayOf(ACCESS_FINE_LOCATION),
                                        PERMISSION_REQUEST_CODE)
                                }
                            }
                            return
                        }
                    }
                }
            }
        }
    }
    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermission()){
            requestPermission()
        }else{
            startLocationUpdates()
        }
    }

    private fun getLocationUpdates()
    {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest()
        locationRequest.interval = 50000
        locationRequest.fastestInterval = 50000
        locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses: List<Address> = geocoder.getFromLocation(locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude, 1)

                    if (addresses != null && addresses.isNotEmpty()) {
                        val cityName = addresses[0].getAddressLine(0)
                        viewmodel.getWeather(q = cityName, unit = Constants.WEATHER_UNIT)
                    }
                }
            }
        }.also { locationCallback = it }
    }
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
}