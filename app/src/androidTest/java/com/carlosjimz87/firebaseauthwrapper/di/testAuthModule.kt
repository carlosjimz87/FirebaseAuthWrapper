package com.carlosjimz87.firebaseauthwrapper.di

import com.carlosjimz87.auth.data.datasource.fakes.FakeFirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.fakes.FakeGoogleAuthDataSource
import com.carlosjimz87.auth.data.datasource.interfaces.FirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.interfaces.GoogleAuthDataSource
import com.carlosjimz87.auth.data.repository.AuthRepositoryImpl
import com.carlosjimz87.auth.domain.repo.AuthRepository
import com.carlosjimz87.auth.presentation.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testAuthModule = module {
    single<FirebaseAuthDataSource> { FakeFirebaseAuthDataSource() }
    single<GoogleAuthDataSource> { FakeGoogleAuthDataSource() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModel<AuthViewModel> { TestAuthViewModel(get()) }
}