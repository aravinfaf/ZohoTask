package com.aravind.zohotask.ui.view.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.aravind.zohotask.R
import com.aravind.zohotask.databinding.ActivityMainBinding
import com.aravind.zohotask.ui.viewmodel.NewsViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val newsViewmodel: NewsViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenCreated {
            newsViewmodel.getAllRecords().collectLatest {
                Log.d("SSS",it.toString())
            }
        }
    }
}