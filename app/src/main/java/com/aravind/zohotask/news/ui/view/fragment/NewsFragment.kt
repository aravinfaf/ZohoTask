package com.aravind.zohotask.news.ui.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aravind.zohotask.R
import com.aravind.zohotask.databinding.FragmentNewsBinding
import com.aravind.zohotask.network.model.NewsModelData
import com.aravind.zohotask.news.ui.view.adapter.NewsAdapter
import com.aravind.zohotask.news.ui.viewmodel.NewsViewmodel
import com.aravind.zohotask.util.Constants
import com.aravind.zohotask.util.Resource
import com.aravind.zohotask.util.Status
import com.aravind.zohotask.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var binding: FragmentNewsBinding? = null
    private val newsViewmodel: NewsViewmodel by viewModels()
    private lateinit var adapter: NewsAdapter
    private var newsList = ArrayList<NewsModelData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNewsBinding.inflate(layoutInflater)
        Constants.SCREEN = "1"
        setHasOptionsMenu(true)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.news)

        newsViewmodel.newsData.observe(viewLifecycleOwner, NewsObserver)
        newsViewmodel.getAllNews()

        binding?.searchLayout?.searchEditText?.addTextChangedListener {
            it?.toString()?.let { it1 -> filterName(it1.lowercase()) }
        }

        binding?.searchLayout?.closeImageview?.setOnClickListener {
            binding?.searchLayout?.searchEditText?.setText("")
            hideKeyboard(requireActivity())
        }
    }

    private val NewsObserver = Observer<Resource<MutableList<NewsModelData>>> {
        when (it.status) {

            Status.LOADING -> {
                binding?.progressBar?.visibility = View.VISIBLE
            }

            Status.SUCCESS -> {
                binding?.progressBar?.visibility = View.GONE

                newsList = it?.data as ArrayList<NewsModelData>
                adapter = NewsAdapter(newsList,
                    object : NewsAdapter.OnAuthorClickListener {
                        override fun onAuthorClicked(news: NewsModelData) {

                            findNavController().navigate(
                                NewsFragmentDirections.newsDetail(news))
                        }
                    })
                binding?.newsRecyclerview?.adapter = adapter
            }
            Status.ERROR -> {
                binding?.progressBar?.visibility = View.GONE
                requireActivity().showToast(it?.message!!)
            }
        }
    }

    fun filterName(textAuthor: String) {
        searchResult(textAuthor)
        val filterList =
            newsList.filter { data -> data.author?.contains(textAuthor)!! } as ArrayList<NewsModelData>
        adapter.setFilter(filterList)
        adapter.notifyDataSetChanged()
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun searchResult(searchString: String) {
        if (searchString.isNotEmpty()) {
            binding?.searchLayout?.closeImageview?.visibility = View.VISIBLE
            binding?.searchLayout?.searchImageview?.visibility = View.INVISIBLE
        } else {
            binding?.searchLayout?.closeImageview?.visibility = View.INVISIBLE
            binding?.searchLayout?.searchImageview?.visibility = View.VISIBLE
        }
    }
}