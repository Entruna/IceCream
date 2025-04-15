package com.example.icecream

import android.app.Application
import android.util.Log
import com.example.icecream.domain.usecase.InitAppDataUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class IceCreamApp : Application() {

    @Inject
    lateinit var initAppDataUseCase: InitAppDataUseCase

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                initAppDataUseCase()
            } catch (e: Exception) {
                Log.e("IceCreamApp", "Failed to init app data", e)
            }
        }
    }
}