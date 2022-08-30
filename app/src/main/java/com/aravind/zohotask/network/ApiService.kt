package com.aravind.zohotask.network

import com.aravind.zohotask.network.model.NewsModel
import com.aravind.zohotask.network.model.WeatherModel
import com.aravind.zohotask.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("news?category=all")
    suspend fun getNews() : NewsModel

    @GET("weather?")
    suspend fun getWeatherDetails(@Query("q") q : String,@Query("units") units : String,
                                  @Query("appid") appid : String,) : Response<WeatherModel>

}