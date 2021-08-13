package com.notnotme.brewdogdiy

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.notnotme.brewdogdiy.repository.datasource.BeerService
import com.notnotme.brewdogdiy.repository.datastore.BeerDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @Provides
    @Singleton
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
    @Provides
    @Singleton
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
    fun provideBeerService(retrofit: Retrofit): BeerService = retrofit.create(BeerService::class.java)

    /**
     * Provide an instance of BeerDao
     */
    @Provides
    @Singleton
    fun provideBeerDao(dataStore: BeerDataStore) = dataStore.beerDao()

    /**
     * Provide an instance of BeerDataStore
     */
    @Provides
    @Singleton
    fun provideBeerDataStore(@ApplicationContext context: Context) = provideBeerDataStore(context, false)

    /**
     * Provide an instance of BeerDataStore
     */
    @Singleton
    fun provideBeerDataStore(@ApplicationContext context: Context, inMemory: Boolean) = if (inMemory) {
        Room.inMemoryDatabaseBuilder(context, BeerDataStore::class.java).build()
    } else {
        Room.databaseBuilder(context, BeerDataStore::class.java, "database").build()
    }

}