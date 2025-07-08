package com.carlosjimz87.firebaseauthwrapper

import android.app.Application
import com.carlosjimz87.auth.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(authModule)
        }
    }
}