package com.aravind.zohotask.appstart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aravind.zohotask.databinding.DashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Dashboard : AppCompatActivity() {

    private var binding : DashboardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DashboardBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}