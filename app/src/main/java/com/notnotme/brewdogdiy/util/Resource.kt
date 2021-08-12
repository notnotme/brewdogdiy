package com.notnotme.brewdogdiy.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Class that handle a resource of type T and can be used to manage
 * network resource or other things that can have a success,error
 * or loading status.
 *
 * @param T The type of resource to manage, must be Parcelable
 */
@Parcelize
data class Resource<out T : Parcelable>(
    val status: Status,
    val data: T?,
    val message: String?
) : Parcelable {

    companion object {
        /**
         * Resource status
         */
        @Parcelize
        enum class Status : Parcelable {
            Success,
            Error,
            Loading
        }

        /**
         * Create a success Resource
         * @param data The data produced by success
         * @return A success Resource
         **/
        fun <T : Parcelable> success(data: T?) = Resource(Status.Success, data, null)

        /**
         * Create a error Resource
         * param message An error message
         * @param data The data produced by error
         * @return A error Resource
         **/
        fun <T : Parcelable> error(message: String, data: T?) = Resource(Status.Error, data, message)

        /**
         * Create a loading Resource
         * @param data The data produced by loading
         * @return A loading Resource
         **/
        fun <T : Parcelable> loading(data: T?) = Resource(Status.Loading, data, null)
    }

}
