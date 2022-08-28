package com.aravind.zohotask.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aravind.zohotask.network.NewsService
import com.aravind.zohotask.network.model.NewsModelData
import com.aravind.zohotask.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewmodel @Inject constructor(
    private val newsService: NewsService,
    private val newsRepository: NewsRepository): ViewModel() {

    val newsLiveData = MutableLiveData<List<NewsModelData>>()

   fun getAllRecords(){
        viewModelScope.launch {
            val news = newsService.getNews()
            newsRepository.deleteAllNews()
            news.data?.forEach { newsData ->
                newsRepository.insertNews(newsData)
            }

            newsLiveData.postValue(newsRepository.getAllNews())
        }
   }
}