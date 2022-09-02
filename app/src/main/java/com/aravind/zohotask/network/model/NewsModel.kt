package com.aravind.zohotask.network.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewsModel(
    @SerializedName("category") var category: String?,
    @SerializedName("data") var data: List<NewsModelData>?,
    @SerializedName("success") var success: Boolean?
)

@Entity(tableName = "newsdetail")
class NewsModelData(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "newsId")
    var newsId : Int = 0,

    @ColumnInfo(name = "author")
    var author: String?,

    @ColumnInfo(name = "content")
    var content: String?,

    @ColumnInfo(name = "date")
    var date: String?,

    @ColumnInfo(name = "id")
    var id: String?,

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String?,

    @ColumnInfo(name = "readMoreUrl")
    var readMoreUrl: String?,

    @ColumnInfo(name = "time")
    var time: String?,

    @ColumnInfo(name = "title")
    var title: String?,

    @ColumnInfo(name = "url")
    var url: String?
) : Serializable