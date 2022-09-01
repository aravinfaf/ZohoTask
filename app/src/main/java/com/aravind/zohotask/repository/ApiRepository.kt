package com.aravind.zohotask.repository

import androidx.lifecycle.LiveData
import com.aravind.zohotask.database.NewsDao
import com.aravind.zohotask.network.ApiService
import com.aravind.zohotask.network.SafeApiRequest
import com.aravind.zohotask.network.model.NewsModel
import com.aravind.zohotask.network.model.NewsModelData
import com.aravind.zohotask.network.model.WeatherModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val appDao: NewsDao,
    private val apiService: ApiService,
) : SafeApiRequest() {

    val getAllNews: List<NewsModelData> = appDao.getAllNews()

    suspend fun insertNews(newsData: List<NewsModelData>) = withContext(Dispatchers.IO) {
        {
            appDao.insertAll(news = newsData)
        }
    }

    suspend fun getNewsDetails(): NewsModel = apiRequest {
        apiService.getNews()
    }

    suspend fun findWeather(q: String, unit: String, appid: String): WeatherModel = apiRequest {
        apiService.getWeatherDetails(q = q, units = unit, appid = appid)
    }
}