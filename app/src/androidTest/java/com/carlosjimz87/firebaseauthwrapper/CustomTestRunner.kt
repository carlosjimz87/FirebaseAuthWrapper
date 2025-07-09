package com.carlosjimz87.firebaseauthwrapper

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        // Use your TestApp instead of the default Application class
        return super.newApplication(cl, "com.carlosjimz87.firebaseauthwrapper.TestApp", context)
    }
}