package com.aravind.zohotask.database

import androidx.paging.DataSource
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

    @Query("SELECT * from newsdetail ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getAll(limit : Int, offset : Int) : List<NewsModelData>
}