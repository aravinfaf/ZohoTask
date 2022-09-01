package com.aravind.zohotask.news.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.aravind.zohotask.R
import com.aravind.zohotask.databinding.ActivityNewsDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {

    private var binding : ActivityNewsDetailBinding? = null
    private val navArg : NewsDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.news_detail)

        binding?.webView?.apply {
            settings.apply {
                javaScriptEnabled = true
            }
            webChromeClient = ChromeClient()
            webViewClient = WebViewClient()
            this.setPadding(0,0,0,0)
            this.settings.javaScriptEnabled = true
        }

         binding?.webView?.loadUrl(navArg.url)
    }

    inner class ChromeClient : WebChromeClient(){
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            binding?.progressBar?.progress = newProgress
            if (newProgress == 100){
                binding?.progressBar?.visibility = View.GONE
            }
        }
    }
}