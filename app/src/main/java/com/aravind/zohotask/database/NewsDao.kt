package com.aravind.zohotask.database

import androidx.paging.PagingSource
import androidx.room.*
import com.aravind.zohotask.network.model.NewsModelData

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: List<NewsModelData>)

    @Query("select * from NewsModelData")
    fun getAllNews(): PagingSource<Int,List<NewsModelData>>
}