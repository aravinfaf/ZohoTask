package com.aravind.zohotask.network

import com.aravind.zohotask.network.model.NewsModel
import retrofit2.http.GET

interface NewsService {

    @GET("news?category=all")
    suspend fun getNews() : NewsModel
}