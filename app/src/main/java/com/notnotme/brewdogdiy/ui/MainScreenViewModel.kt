package com.notnotme.brewdogdiy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import com.notnotme.brewdogdiy.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Main screen.
 * Featuring a pager to obtain  a paged beer result
 * @param beerRepository An instance of ApiRepository
 */
@HiltViewModel
@ExperimentalPagingApi
class MainScreenViewModel @Inject constructor(
    private val beerRepository: BeerRepository
) : ViewModel() {

    companion object {
        const val TAG = "MainActivityViewModel"
    }

    private val _downloadStatus = MutableStateFlow<DownloadStatus?>(null)
    val downloadStatus: StateFlow<DownloadStatus?> get() = _downloadStatus

    init {
        viewModelScope.launch {
            beerRepository.getDownloadStatus().collect {
                _downloadStatus.value = it
            }
        }
    }

}
