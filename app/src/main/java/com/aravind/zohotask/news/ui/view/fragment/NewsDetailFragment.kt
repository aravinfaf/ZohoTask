package com.aravind.zohotask.news.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aravind.zohotask.databinding.FragmentNewsDetailBinding
import com.aravind.zohotask.util.loadImage

class NewsDetailFragment : Fragment() {

    private var binding: FragmentNewsDetailBinding? = null
    private val args: NewsDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNewsDetailBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()

        binding?.openWebSite?.setOnClickListener {
            findNavController().navigate(NewsDetailFragmentDirections.webviewFrag(args.news.readMoreUrl!!))
        }
    }

    private fun bindData() {

        binding?.apply {
            articleImage.loadImage(args.news.imageUrl)

            authorNameTxt.text = args.news.author
            titleTxt.text = args.news.title
            dateTxt.text = args.news.date
            descriptionTxt.text = args.news.content
        }
    }
}