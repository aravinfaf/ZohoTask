package com.aravind.zohotask.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.aravind.zohotask.database.NewsDao
import com.aravind.zohotask.network.ApiService
import com.aravind.zohotask.paging.PagingSource
import com.aravind.zohotask.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewmodel @Inject constructor(
    private val newsService: ApiService,
    private val newsRepository: ApiRepository,
    private val dao: NewsDao,
) : ViewModel() {

    fun getAllRecords() {
        viewModelScope.launch {
            val news = newsService.getNews()
            news.data?.forEach { newsData ->
                newsRepository.insertNews(newsData)
            }
        }
    }

    fun deleteData() {
        viewModelScope.launch {
            newsRepository.deleteAllNews()
        }
    }

    val data = Pager(
        PagingConfig(
            pageSize = 5,
            enablePlaceholders = false,
            initialLoadSize = 5
        ),
    ) {
        PagingSource(dao)
    }.flow.cachedIn(viewModelScope)
}