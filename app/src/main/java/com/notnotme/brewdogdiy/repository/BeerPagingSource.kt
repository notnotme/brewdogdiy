package com.notnotme.brewdogdiy.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.notnotme.brewdogdiy.model.Beer
import kotlinx.coroutines.flow.collect

/**
 * A PagingSource doing request for beers against the backend API
 * @param apiRepository A repository able to request the backend
 */
class BeerPagingSource(
    private val apiRepository: ApiRepository
) : PagingSource<Int, Beer>() {

    override val jumpingSupported = true

    override fun getRefreshKey(state: PagingState<Int, Beer>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Beer> {
        val nextPage = params.key ?: 1
        var result: LoadResult<Int, Beer>? = null

        try {
            apiRepository.getBeers(nextPage, params.loadSize).collect {
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