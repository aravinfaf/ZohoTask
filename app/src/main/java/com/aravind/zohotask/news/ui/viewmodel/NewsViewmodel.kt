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

                    for (i in newsRepository.getNewsDetails().data!!){
                        val item = NewsModelData(
                           author =  i.author ,
                            content = i.content,
                            date = i.date,
                            id = i.id,
                            imageUrl = i.imageUrl,
                            readMoreUrl = i.readMoreUrl,
                            time = i.time,
                            title = i.title,
                            url = i.url
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