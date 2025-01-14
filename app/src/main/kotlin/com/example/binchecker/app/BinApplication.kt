package com.example.binchecker.app

import android.app.Application
import com.example.binchecker.di.appModule
import com.example.data.dataModule
import com.example.domain.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BinApplication)
            androidLogger()
            modules(appModule, domainModule, dataModule)
        }
    }
}