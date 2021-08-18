package com.notnotme.brewdogdiy.ui.beer

import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.util.Resource

/**
 * BeerScreen view states
 */
data class ViewState(
    val beerResource: Resource<Beer>? = null,
    val errorMessage: String? = null
)
