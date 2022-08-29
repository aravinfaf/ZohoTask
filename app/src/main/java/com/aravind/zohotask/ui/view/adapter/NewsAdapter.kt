package com.aravind.zohotask.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aravind.zohotask.databinding.AdapterItemBinding
import com.aravind.zohotask.network.model.NewsModelData


class NewsAdapter : PagingDataAdapter<NewsModelData,NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsModelData>(){
            override fun areItemsTheSame(oldItem: NewsModelData, newItem: NewsModelData): Boolean =
                oldItem.newsId == newItem.newsId

            override fun areContentsTheSame(
                oldItem: NewsModelData,
                newItem: NewsModelData
            ): Boolean = oldItem == newItem

        }
    }

    inner class NewsViewHolder(val binding: AdapterItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            textView.text = item?.author
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            AdapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )    }

}