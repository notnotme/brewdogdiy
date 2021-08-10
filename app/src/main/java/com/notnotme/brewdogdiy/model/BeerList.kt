package com.notnotme.brewdogdiy.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * BeerList type representation
 */
@Parcelize
class BeerList : ArrayList<Beer>(), Parcelable