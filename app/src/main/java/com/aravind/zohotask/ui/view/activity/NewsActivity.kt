package com.aravind.zohotask.ui.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.aravind.zohotask.R
import com.aravind.zohotask.databinding.ActivityNewsBinding
import com.aravind.zohotask.ui.view.adapter.NewsAdapter
import com.aravind.zohotask.ui.view.adapter.NewsLoadStateAdapter
import com.aravind.zohotask.ui.viewmodel.NewsViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    private var binding : ActivityNewsBinding? = null
    private val newsViewmodel: NewsViewmodel by viewModels()
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        newsViewmodel.getAllRecords()
        adapter = NewsAdapter()

            lifecycleScope.launchWhenCreated {
                newsViewmodel.data.collectLatest {
                    adapter.submitData(it)
                }
            }
        binding?.newsRecyclerview?.adapter = adapter.withLoadStateFooter(
            NewsLoadStateAdapter()
        )
    }
}