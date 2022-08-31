package com.aravind.zohotask.network

import com.aravind.zohotask.network.model.NewsModel
import com.aravind.zohotask.network.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("news?category=all")
    suspend fun getNews() : Response<NewsModel>

    @GET("weather?")
    suspend fun getWeatherDetails(@Query("q") q : String,@Query("units") units : String,
                                  @Query("appid") appid : String,) : Response<WeatherModel>

}