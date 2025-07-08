package com.carlosjimz87.firebaseauthwrapper

import com.carlosjimz87.firebaseauthwrapper.di.testAuthModule
import org.koin.core.module.Module

class TestApplication : App() {
    override val appModules: List<Module>
        get() = listOf(testAuthModule)
}