package com.aravind.zohotask.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.aravind.zohotask.database.NewsDao
import com.aravind.zohotask.network.model.NewsModel
import com.aravind.zohotask.paging.PagingSource
import com.aravind.zohotask.repository.ApiRepository
import com.aravind.zohotask.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewsViewmodel @Inject constructor(
    private val newsRepository: ApiRepository,
    private val dao: NewsDao,
) : ViewModel() {

    var newsLiveData = MutableLiveData<Resource<NewsModel>>()

    fun getAllNews() {
        newsLiveData.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                newsLiveData.postValue(Resource.success(newsRepository.getNewsDetails()))
            }catch (e : Exception) {
                withContext(Dispatchers.Main) {
                    newsLiveData.value = Resource.error(e.message!!, null)
                }
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