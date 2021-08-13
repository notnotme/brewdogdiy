package com.notnotme.brewdogdiy.repository.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.notnotme.brewdogdiy.model.Beer

/**
 * A PagingSource doing request for beers against the backend API
 * @param apiDataSource A datasource able to request the backend
 */
class BeerPagingSource(
    private val apiDataSource: ApiDataSource
) : PagingSource<Int, Beer>() {

    override val jumpingSupported = true

    override fun getRefreshKey(state: PagingState<Int, Beer>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Beer> {
        val nextPage = params.key ?: 1
        var result: LoadResult<Int, Beer>?

        try {
            apiDataSource.getBeers(nextPage, params.loadSize).let {
                val body = it.body()
                if (it.isSuccessful && body != null) {
                    result = LoadResult.Page(
                        data = body,
                        prevKey = if (nextPage == 1) null else nextPage - 1,
                        nextKey = if (body.size < params.loadSize) null else nextPage + 1
                    )
                } else {
                    result = LoadResult.Error(Exception(it.message()))
                }
            }
        } catch (exception: Exception) {
            result = LoadResult.Error(exception)
        }

        return result!!
    }

}