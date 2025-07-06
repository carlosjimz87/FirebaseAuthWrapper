package com.carlosjimz87.auth

import com.carlosjimz87.auth.data.datasource.FirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.google.GoogleAuthDataSource
import com.carlosjimz87.auth.data.repository.AuthRepositoryImpl
import com.carlosjimz87.auth.domain.repo.AuthRepository
import com.carlosjimz87.auth.presentation.AuthViewModel
import org.koin.dsl.module
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel

val authModule = module {

    single { FirebaseAuth.getInstance() }
    single { FirebaseAuthDataSource(get()) }
    single { GoogleAuthDataSource(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModel { AuthViewModel(get()) }
}