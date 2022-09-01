package com.aravind.zohotask.news.ui.view.fragment

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.aravind.zohotask.R
import com.aravind.zohotask.databinding.ActivityNewsBinding
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

    private var binding: ActivityNewsBinding? = null
    private val newsViewmodel: NewsViewmodel by viewModels()
    private lateinit var adapter: NewsAdapter
    private var newsList = ArrayList<NewsModelData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = ActivityNewsBinding.inflate(layoutInflater)
        Constants.SCREEN = "1"
        return binding?.root!!

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.news)


        newsViewmodel.getAllNews()
        newsViewmodel.newsData.observe(viewLifecycleOwner, NewsObserver)

        binding?.searchLayout?.searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                filterName(text.toString().lowercase())
            }
        })


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
                                NewsFragmentDirections.newsDetail(news.readMoreUrl!!))
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
        val filterList = ArrayList<NewsModelData>()
        for (name in newsList) {
            if (name.author?.lowercase()?.contains(textAuthor)!!) {
                filterList.add(name)
            }
        }
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