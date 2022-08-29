package com.aravind.zohotask.repository

import com.aravind.zohotask.database.NewsDao
import com.aravind.zohotask.network.model.NewsModelData
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val appDao: NewsDao,
) {

    fun getAllNews(): List<NewsModelData> {
        return appDao.getAllNews()
    }

    fun insertNews(newsData: NewsModelData) {
        appDao.insertAll(news = newsData)
    }

    fun deleteAllNews(){
        appDao.deleteAllRecords()
    }

}