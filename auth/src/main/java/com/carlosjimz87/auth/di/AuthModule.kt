package com.carlosjimz87.auth.di

import com.carlosjimz87.auth.data.datasource.RealFirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.RealGoogleAuthDataSource
import com.carlosjimz87.auth.data.datasource.interfaces.FirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.interfaces.GoogleAuthDataSource
import com.carlosjimz87.auth.data.repository.AuthRepositoryImpl
import com.carlosjimz87.auth.domain.repo.AuthRepository
import com.carlosjimz87.auth.presentation.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { FirebaseAuth.getInstance() }
    single<FirebaseAuthDataSource> { RealFirebaseAuthDataSource(get()) }
    single<GoogleAuthDataSource> { RealGoogleAuthDataSource(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModel { AuthViewModel(get()) }
}