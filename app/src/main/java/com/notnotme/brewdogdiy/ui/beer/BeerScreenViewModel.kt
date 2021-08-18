package com.notnotme.brewdogdiy.ui.beer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.repository.BeerRepository
import com.notnotme.brewdogdiy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerScreenViewModel @Inject constructor(
    private val beerRepository: BeerRepository
) : ViewModel() {

    companion object {
        const val TAG = "BeerScreenViewModel"
    }

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> get() = _state

    private val beerResource = MutableStateFlow<Resource<Beer>?>(null)
    private val errorMessage = MutableStateFlow<String?>(null)

    init {
        // Combines the latest value from each flow to generate a new ViewState
        viewModelScope.launch {
            combine(
                beerResource,
                errorMessage
            ) { beerResource, errorMessage ->
                ViewState(
                    beerResource = beerResource,
                    errorMessage = errorMessage
                )
            }.catch { e ->
                Log.e(TAG, "Error: ${e.message}")
            }.collect {
                _state.value = it
            }
        }
    }

    /**
     * @param beerId A beer ID, 0, or null
     * @return A beer by it's ID or a random beer if the ID is null or equals 0
     */
    fun getBeerOrRandom(beerId: Long?) {
        viewModelScope.launch {
            errorMessage.value = null
            beerResource.value = Resource.loading(null)

            val flow = when (beerId) {
                0L, null -> beerRepository.getRandomBeerFromDao()
                else -> beerRepository.getBeerFromDao(beerId)
            }

            val resource = flow.catch { exception ->
                val message = exception.message ?: "Unknown error"
                beerResource.value = Resource.error(message, null)
                errorMessage.value = message
            }.first()

            if (resource == null) {
                val message = "No beers received"
                beerResource.value = Resource.error(message, null)
                errorMessage.value = message
            } else {
                beerResource.value = Resource.success(resource)
            }
        }
    }

}