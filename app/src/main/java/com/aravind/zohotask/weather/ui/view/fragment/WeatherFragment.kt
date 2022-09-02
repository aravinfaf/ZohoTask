package com.aravind.zohotask.weather.ui.view.fragment

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import coil.transform.CircleCropTransformation
import com.aravind.zohotask.R
import com.aravind.zohotask.databinding.ActivityWeatherBinding
import com.aravind.zohotask.network.model.WeatherModel
import com.aravind.zohotask.util.*
import com.aravind.zohotask.weather.ui.viewmodel.WeatherViewmodel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var binding: ActivityWeatherBinding? = null
    private val viewmodel: WeatherViewmodel by viewModels()

    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var address: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Constants.SCREEN = "2"
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.weather)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewmodel.weatherLiveData.observe(viewLifecycleOwner, weatherObserver)

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

                        imageWeatherSymbol.load(Constants.WEATHER_API_IMAGE
                                + "${iconCode}@4x.png") {
                            crossfade(true)
                            placeholder(R.drawable.placeholder)
                        }
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


    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(requireContext(),
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {

            // request permission
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE);

            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude

                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val list: List<Address> =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (list[0].locality == null || list[0].locality.isNullOrEmpty()) {
                        address = list[0].subAdminArea
                    } else {
                        address = list[0].locality
                    }

                    viewmodel.getWeather(q = address!!, unit = Constants.WEATHER_UNIT)
                }
            }
            .addOnFailureListener {
                requireActivity().showToast(getString(R.string.failed_getlocation))
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission granted
                } else {
                    // permission denied
                    requireActivity().showToast(getString(R.string.grant_permission))
                  }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getCurrentLocation()
    }
}