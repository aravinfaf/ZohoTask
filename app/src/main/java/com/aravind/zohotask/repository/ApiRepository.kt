package com.aravind.zohotask.repository

import com.aravind.zohotask.database.NewsDao
import com.aravind.zohotask.network.ApiService
import com.aravind.zohotask.network.SafeApiRequest
import com.aravind.zohotask.network.model.NewsModel
import com.aravind.zohotask.network.model.NewsModelData
import com.aravind.zohotask.network.model.WeatherModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val appDao: NewsDao,
    private val apiService: ApiService
) : SafeApiRequest() {

    fun getAllNews(): List<NewsModelData> {
        return appDao.getAllNews()
    }

    fun insertNews(newsData: NewsModelData) {
        CoroutineScope(Dispatchers.IO).launch {
            appDao.insertAll(news = newsData)
        }
    }

    fun deleteAllNews(){
        appDao.deleteAllRecords()
    }

    suspend fun getNewsDetails() : NewsModel = apiRequest {
        apiService.getNews()
    }

    suspend fun findWeather(q: String,unit : String, appid : String) : WeatherModel = apiRequest{
        apiService.getWeatherDetails(q = q, units = unit, appid = appid)
    }
}