package com.aravind.zohotask.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aravind.zohotask.network.model.NewsModelData

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: NewsModelData)

    @Query("SELECT * from newsdetail")
    fun getAllNews(): List<NewsModelData>

    @Query("DELETE FROM newsdetail")
    fun deleteAllRecords()
}