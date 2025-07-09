package com.carlosjimz87.firebaseauthwrapper

import android.app.Application
import com.carlosjimz87.auth.di.authModule
import com.carlosjimz87.firebaseauthwrapper.di.testAuthModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

open class TestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TestApp)
            modules(authModule + testAuthModule)
        }
    }
}