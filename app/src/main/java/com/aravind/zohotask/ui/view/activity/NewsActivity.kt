package com.aravind.zohotask.ui.view.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aravind.zohotask.databinding.ActivityNewsBinding
import com.aravind.zohotask.network.model.NewsModel
import com.aravind.zohotask.network.model.NewsModelData
import com.aravind.zohotask.ui.view.adapter.NewsAdapter
import com.aravind.zohotask.ui.viewmodel.NewsViewmodel
import com.aravind.zohotask.util.Resource
import com.aravind.zohotask.util.Status
import com.aravind.zohotask.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    private var binding : ActivityNewsBinding? = null
    private val newsViewmodel: NewsViewmodel by viewModels()
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        newsViewmodel.newsLiveData.observe(this,newsObserver)

        newsViewmodel.getAllNews()
    }
    private val newsObserver = Observer<Resource<NewsModel>>{
        when(it.status){
            Status.LOADING -> {}
            Status.ERROR -> {showToast(it.message!!)}
            Status.SUCCESS -> {
                adapter = NewsAdapter(it?.data?.data!!,object : NewsAdapter.onAuthorClickListener{
                    override fun onAuthorClicked(news: NewsModelData) {

                    }
                })
                Log.d("SSS",it?.data?.category!!)

                binding?.newsRecyclerview?.adapter = adapter
            }
        }
    }
}