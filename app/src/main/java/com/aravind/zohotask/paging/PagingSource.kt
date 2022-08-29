package com.aravind.zohotask.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aravind.zohotask.database.NewsDao
import com.aravind.zohotask.network.model.NewsModelData
import kotlinx.coroutines.delay
import javax.inject.Inject

class PagingSource @Inject constructor(
    private val newsDao: NewsDao
) : PagingSource<Int, NewsModelData>() {


    override fun getRefreshKey(state: PagingState<Int, NewsModelData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage =state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?:anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsModelData> {
        val page = params.key ?: 0

        return try {
            val entities =newsDao.getAll(limit = params.loadSize,offset = page*params.loadSize)
            if (page != 0) delay(1000)
                LoadResult.Page(
                    data = entities,
                    prevKey = if (page == 0) null else page -1,
                    nextKey = if (entities.isEmpty()) null else page +1
                )
        }
        catch (e : Exception){
            LoadResult.Error(e)
        }
    }

}