package com.notnotme.brewdogdiy

import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : android.app.Application() {

    companion object {
        const val TAG = "Application"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "All your base are belong to us.")
    }

}