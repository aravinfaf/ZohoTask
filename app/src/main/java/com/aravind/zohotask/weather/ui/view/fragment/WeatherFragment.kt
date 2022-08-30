package com.aravind.zohotask.weather.ui.view.fragment

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aravind.zohotask.R
import com.aravind.zohotask.databinding.ActivityWeatherBinding
import com.aravind.zohotask.network.model.WeatherModel
import com.aravind.zohotask.util.*
import com.aravind.zohotask.weather.ui.viewmodel.WeatherViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class WeatherFragment : AppCompatActivity() {

    private var binding: ActivityWeatherBinding? = null
    private val viewmodel: WeatherViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewmodel.weatherLiveData.observe(this, weatherObserver)

        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(11.1085, 11.1085, 1)
        val cityName: String = addresses?.get(0).getLocality()
        Log.d("AAA",cityName)

        viewmodel.getWeather(q = cityName, unit = Constants.WEATHER_UNIT)

    }

    private val weatherObserver = Observer<Resource<WeatherModel>> {
        when (it.status) {
            Status.LOADING -> {

            }
            Status.SUCCESS -> {

                it.data?.let { weatherModel ->
                    val iconCode = weatherModel?.weather?.first().icon?.replace("n", "d")
                    changeBgAccToTemp(iconCode)
                    binding?.apply {
                        AppUtils.setGlideImageview(imageWeatherSymbol, Constants.WEATHER_API_IMAGE
                                + "${iconCode}@4x.png")

                        textTodaysDate?.text = AppUtils.getCurrentDateTime(Constants.DATE_FORMAT)

                        textTemperature?.text = weatherModel?.main?.temp.toString()

                        binding?.textCityName?.text =
                            weatherModel.name + ", " + weatherModel?.sys?.country
                    }
                }
            }

            Status.ERROR -> {
                showToast(it?.message!!)
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
}