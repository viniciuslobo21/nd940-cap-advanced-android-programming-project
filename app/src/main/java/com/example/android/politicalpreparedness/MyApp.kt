package com.example.android.politicalpreparedness


import androidx.multidex.MultiDexApplication
import com.example.android.politicalpreparedness.di.Module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(Module.getModule()))
        }
    }
}