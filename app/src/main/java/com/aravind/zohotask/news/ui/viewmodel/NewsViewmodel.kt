package com.aravind.zohotask.news.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aravind.zohotask.network.model.NewsModelData
import com.aravind.zohotask.repository.ApiRepository
import com.aravind.zohotask.util.NetworkUtil
import com.aravind.zohotask.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewmodel @Inject constructor(
    private val newsRepository: ApiRepository,
    private val networkUtil: NetworkUtil,
) : ViewModel() {

    val newsData : MutableLiveData<Resource<MutableList<NewsModelData>>> = MutableLiveData()

    fun getAllNews() {
        viewModelScope.launch(Dispatchers.IO) {
            newsData.postValue(Resource.loading())
            try {
                var dplist = newsRepository.getAllNews
                if (dplist.isEmpty() && networkUtil.isNetworkConnected()) {
                    val insertingData = mutableListOf<NewsModelData>()

                    for (item in newsRepository.getNewsDetails().data!!){
                        val item = NewsModelData(
                           author =  item.author ,
                            content = item.content,
                            date = item.date,
                            id = item.id,
                            imageUrl = item.imageUrl,
                            readMoreUrl = item.readMoreUrl,
                            time = item.time,
                            title = item.title,
                            url = item.url
                        )
                        insertingData.add(item)
                        newsRepository.insertNews(insertingData)
                    }
                    newsData.postValue(Resource.success(insertingData))
                } else {
                    newsData.postValue(Resource.success(dplist) as Resource<MutableList<NewsModelData>>?)
                }
            } catch (ex: Exception) {
                newsData.postValue(Resource.error(ex.toString(), null))
            }
        }
    }
}