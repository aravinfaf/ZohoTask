package com.aravind.zohotask.ui.view.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.aravind.zohotask.R
import com.aravind.zohotask.databinding.ActivityMainBinding
import com.aravind.zohotask.network.model.NewsModelData
import com.aravind.zohotask.ui.viewmodel.NewsViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    private val newsViewmodel: NewsViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsViewmodel.newsLiveData.observe(this,NewsObserver)
        newsViewmodel.getAllRecords()
    }

    private var NewsObserver = Observer<List<NewsModelData>>{
        Log.d("SS",it.toString())
    }
}