package com.notnotme.brewdogdiy

import com.google.gson.GsonBuilder
import com.notnotme.brewdogdiy.repository.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    /**
     * This is the base url of the Api
     */
    @Provides
    fun provideBaseUrl() = "https://api.punkapi.com/"

    /**
     * Provide an OkHttpClient which will log each request when in debug mode
     * and will stay silent otherwise
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    } else {
        OkHttpClient.Builder()
            .build()
    }

    /**
     * Provide a Retrofit that use a GsonConverterFactory and an instance of
     * RetrofitWebService.EnumConverterFactory to allow enum class to be serialized
     */
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()


    /**
     * Provide an instance of RetrofitWebService
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

}