package com.aravind.zohotask.news.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aravind.zohotask.databinding.AdapterListBinding
import com.aravind.zohotask.network.model.NewsModelData
import com.aravind.zohotask.util.loadImage

class NewsAdapter(
    private var newsList : List<NewsModelData>,
    val clickListener: OnAuthorClickListener
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: AdapterListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(data : NewsModelData) {

            binding.nameTextview.text = data.author
            binding.descriptionTextview.text = data.content
            binding.dateTextview.text = data.date
            binding.avatarImageview.loadImage(data.imageUrl)

            binding.descriptionTextview.setOnClickListener {
                clickListener.onAuthorClicked(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    fun setFilter(list : ArrayList<NewsModelData>){
        this.newsList = list
    }

    interface OnAuthorClickListener{
        fun onAuthorClicked(news : NewsModelData)
    }
}

