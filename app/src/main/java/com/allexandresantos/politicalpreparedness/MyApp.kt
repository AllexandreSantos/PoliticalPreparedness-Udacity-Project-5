package com.allexandresantos.politicalpreparedness

import android.app.Application
import com.allexandresantos.politicalpreparedness.di.dataModule
import com.allexandresantos.politicalpreparedness.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(viewModelModule, dataModule))
        }
    }

}