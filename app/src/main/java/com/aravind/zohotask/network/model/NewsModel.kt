package com.aravind.zohotask.network.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class NewsModel(
    @SerializedName("category") var category: String?,
    @SerializedName("data") var data: List<NewsModelData>?,
    @SerializedName("category") var success: Boolean?
)

@Entity
data class NewsModelData(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var newsId : Int,
    @SerializedName("author") var author: String?,
    @SerializedName("content") var content: String?,
    @SerializedName("data") var date: String?,
    @SerializedName("id") var id: String?,
    @SerializedName("imageUrl") var imageUrl: String?,
    @SerializedName("readMoreUrl") var readMoreUrl: String?,
    @SerializedName("time") var time: String?,
    @SerializedName("title") var title: String?,
    @SerializedName("url") var url: String?
)