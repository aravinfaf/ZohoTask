package com.aravind.zohotask.weather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aravind.zohotask.network.model.WeatherModel
import com.aravind.zohotask.repository.ApiRepository
import com.aravind.zohotask.util.ApiException
import com.aravind.zohotask.util.Constants
import com.aravind.zohotask.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherViewmodel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    var weatherLiveData = MutableLiveData<Resource<WeatherModel>>()

    fun getWeather(q : String, unit : String) {
        weatherLiveData.value = Resource.loading(null)

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    weatherLiveData.postValue(
                        Resource.success(apiRepository.findWeather(q = q, unit = unit,
                            appid = Constants.APP_KEY)))

                } catch (e: ApiException) {
                    withContext(Dispatchers.Main) {
                        weatherLiveData.postValue( e.message.let { Resource.error(it!!, null) })
                    }
                }
            }
    }
}