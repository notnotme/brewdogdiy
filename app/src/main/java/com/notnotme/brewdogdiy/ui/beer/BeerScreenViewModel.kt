package com.notnotme.brewdogdiy.ui.beer

import androidx.lifecycle.ViewModel
import com.notnotme.brewdogdiy.model.Beer
import com.notnotme.brewdogdiy.repository.ApiRepository
import com.notnotme.brewdogdiy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * ViewModel for Beer screen.
 * Featuring methods to get a random beer and by Id
 * @param apiRepository An instance of ApiRepository
 */
@HiltViewModel
@ExperimentalCoroutinesApi
class BeerScreenViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    /**
     * Get a random beer from the backend API and produce a Resource<Beer>
     * @return A producer of Resource<Beer>
     */
    fun getRandomBeer() = channelFlow<Resource<Beer>> {
        channel.send(Resource.loading(null))
        apiRepository.getRandomBeer().collectLatest {
            val body = it.body()
            if (!it.isSuccessful || body == null) {
                channel.send(Resource.error(it.message(), null))
            } else {
                channel.send(Resource.success(body[0]))
            }
        }
    }.catch { exception ->
        emit(Resource.error(exception.message?:"Unknown error", null))
    }.flowOn(Dispatchers.IO)

    /**
     * Get a beer from the backend API by Id
     * @return A producer of Resource<Beer>
     */
    fun getBeer(beerId: Long) = channelFlow<Resource<Beer>> {
        channel.send(Resource.loading(null))
        apiRepository.getBeer(beerId).collectLatest {
            val body = it.body()
            if (!it.isSuccessful || body == null) {
                channel.send(Resource.error(it.message(), null))
            } else {
                channel.send(Resource.success(body[0]))
            }
        }
    }.catch { exception ->
        emit(Resource.error(exception.message?:"Unknown error", null))
    }.flowOn(Dispatchers.IO)

}
