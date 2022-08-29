package com.aravind.zohotask.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.aravind.zohotask.database.NewsDao
import com.aravind.zohotask.network.NewsService
import com.aravind.zohotask.network.model.NewsModelData
import com.aravind.zohotask.paging.PagingSource
import com.aravind.zohotask.repository.NewsRepository
import com.aravind.zohotask.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewmodel @Inject constructor(
    private val newsService: NewsService,
    private val newsRepository: NewsRepository,
    private val dao: NewsDao,
) : ViewModel() {

    fun getAllRecords() {
        viewModelScope.launch {
            val news = newsService.getNews()
            newsRepository.deleteAllNews()
            news.data?.forEach { newsData ->
                newsRepository.insertNews(newsData)
            }
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

    private fun getPagedListConfig(): PagedList.Config {

        return PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(Constants.PAGE_INITIAL_LOAD_SIZE_HINT)
            .setPrefetchDistance(Constants.PAGE_PREFETCH_DISTANCE)
            .setEnablePlaceholders(false)
            .build()
    }
}