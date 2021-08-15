package com.notnotme.brewdogdiy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import com.notnotme.brewdogdiy.repository.BeerRepository
import com.notnotme.brewdogdiy.util.Resource
import com.notnotme.brewdogdiy.util.StringKt.contentOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for List screen.
 * Featuring a pager to obtain  a paged beer result
 * @param beerRepository An instance of ApiRepository
 */
@HiltViewModel
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class MainActivityViewModel @Inject constructor(
    private val beerRepository: BeerRepository
) : ViewModel() {

    companion object {
        const val TAG = "MainActivityViewModel"
        const val DOWNLOAD_CHUNKS = 80
    }

    private val _downloadStatus = MutableStateFlow<DownloadStatus?>(null)
    val downloadStatus: StateFlow<DownloadStatus?> get() = _downloadStatus

    private val _downloadUpdateStatus = MutableStateFlow<DownloadStatus?>(null)
    val downloadUpdateStatus: StateFlow<DownloadStatus?> get() = _downloadUpdateStatus

    /**
     * A Pager that can display all beers stored in the database
     */
    val beerPager = Pager(
        config = PagingConfig(
            pageSize = 25,
            initialLoadSize = 25,
            maxSize = 100
        ),
        remoteMediator = null,
        pagingSourceFactory = { beerRepository.getBeersFromDao() },
    ).flow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _downloadStatus.value = beerRepository.getDownloadStatus(1L)
        }
    }

    /**
     * @param beerId A beer ID, 0, or null
     * @return A beer by it's ID or a random beer if the ID is null or equals 0
     */
    fun getBeerOrRandom(beerId: Long?) = when (beerId) {
        0L, null -> getRandomBeer()
        else -> getBeer(beerId)
    }

    /**
     * Get a random beer
     * @return A Flow<Resource<Beer>>
     */
    private fun getRandomBeer() = flow {
        emit(Resource.loading(null))
        beerRepository.getRandomBeerFromDao().collectLatest {
            if (it == null) {
                error("No beers received")
            } else {
                emit(Resource.success(it))
            }
        }
    }.catch { exception ->
        emit(Resource.error(exception.message ?: "Unknown error", null))
    }.flowOn(Dispatchers.IO)

    /**
     * Start the process of downloading all beers from the remote server
     */
    fun updateAllFromRemote() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e(TAG, "Start download")
            var currentPage = 1
            val downloadStatus = DownloadStatus(2L, Date(System.currentTimeMillis()), 0, 1, false)

            _downloadUpdateStatus.value = downloadStatus
            while (!downloadStatus.isFinished) {
                Log.e(TAG, "loop: $downloadStatus")
                try {

                    val response = beerRepository.getBeersFromRemote(currentPage, DOWNLOAD_CHUNKS)
                    val body = response.body()
                    if (!response.isSuccessful || body == null) {
                        error(response.message().contentOrNull() ?: "Unknown error")
                    }

                    beerRepository.runInTransaction {
                        val savedCount = beerRepository.saveBeersToDao(body)
                        downloadStatus.page = currentPage
                        downloadStatus.totalBeers += savedCount.size
                        if (body.size < DOWNLOAD_CHUNKS) {
                            downloadStatus.isFinished = true
                        } else {
                            currentPage += 1
                        }
                        beerRepository.saveDownloadStatus(downloadStatus)
                    }

                    _downloadUpdateStatus.value = downloadStatus
                } catch (exception: Exception) {
                    error(exception.message?.contentOrNull() ?: "Unknown error")
                }
            }
            beerRepository.deleteDownloadStatus(downloadStatus.id)

            Log.e(TAG, "Finished")
            _downloadUpdateStatus.value = downloadStatus
        }
    }

    /**
     * Get a beer by ID
     * @return A Flow<Resource<Beer>>
     */
    private fun getBeer(beerId: Long) = flow {
        emit(Resource.loading(null))
        beerRepository.getBeerFromDao(beerId).collectLatest {
            emit(Resource.success(it))
        }
    }.catch { exception ->
        emit(Resource.error(exception.message ?: "Unknown error", null))
    }.flowOn(Dispatchers.IO)

}