package com.aravind.zohotask.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.aravind.zohotask.R
import com.aravind.zohotask.databinding.AdapterItemBinding
import com.aravind.zohotask.network.model.NewsModelData


class NewsAdapter(
    private var newsList : List<NewsModelData>,
    val clickListener: onAuthorClickListener
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: AdapterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data : NewsModelData) {

            binding?.nameTextview.text = data.author
            binding?.descriptionTextview.text = data.content

            binding.avatarImageview.load(data.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.placeholder)
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    fun setFilter(list : ArrayList<NewsModelData>){
        this.newsList = list
    }

    interface onAuthorClickListener{
        fun onAuthorClicked(news : NewsModelData)
    }
}

