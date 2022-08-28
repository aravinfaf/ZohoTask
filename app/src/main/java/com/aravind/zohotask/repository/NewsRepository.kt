package com.aravind.zohotask.repository

import androidx.paging.PagingSource
import com.aravind.zohotask.database.NewsDao
import com.aravind.zohotask.network.model.NewsModelData
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val appDao: NewsDao
) {

    fun getAllRecords(): PagingSource<Int, List<NewsModelData>> {
        return appDao.getAllNews()
    }

    fun insertRecord(newsData: List<NewsModelData>) {
        appDao.insertAll(news = newsData)
    }
}