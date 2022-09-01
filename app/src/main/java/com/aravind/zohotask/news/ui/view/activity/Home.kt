package com.aravind.zohotask.news.ui.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aravind.zohotask.R
import com.aravind.zohotask.databinding.HomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : Fragment() {

    private var binding: HomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = HomeBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            newsCard.setOnClickListener {
                findNavController().navigate(R.id.newsFragment)
            }

            weatherCard.setOnClickListener {
                findNavController().navigate(R.id.weatherFragment)
            }
        }
    }
}