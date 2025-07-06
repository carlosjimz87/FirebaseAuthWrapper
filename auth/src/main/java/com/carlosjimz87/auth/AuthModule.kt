package com.carlosjimz87.auth

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.google.firebase.auth.FirebaseAuth

val authModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseAuthDataSource(get()) }
    single { GoogleAuthDataSource(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModel { LoginViewModel(get()) }
}