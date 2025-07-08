package com.carlosjimz87.auth.di

import com.carlosjimz87.auth.data.datasource.fakes.FakeFirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.fakes.FakeGoogleAuthDataSource
import com.carlosjimz87.auth.data.datasource.interfaces.FirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.interfaces.GoogleAuthDataSource
import org.koin.dsl.module

val testAuthModule = module {
    single<FirebaseAuthDataSource> { FakeFirebaseAuthDataSource() }
    single<GoogleAuthDataSource> { FakeGoogleAuthDataSource() }
}