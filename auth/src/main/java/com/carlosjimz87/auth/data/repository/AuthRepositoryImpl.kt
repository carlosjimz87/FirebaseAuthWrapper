package com.carlosjimz87.auth.data.repository

import com.carlosjimz87.auth.data.datasource.FirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.google.GoogleAuthDataSource
import com.carlosjimz87.auth.domain.model.AuthUser
import com.carlosjimz87.auth.domain.repo.AuthRepository

class AuthRepositoryImpl(
    private val firebase: FirebaseAuthDataSource,
    private val google: GoogleAuthDataSource? = null
) : AuthRepository {

    override suspend fun signInWithEmail(email: String, password: String) = firebase.signInWithEmail(email, password)

    override suspend fun signUpWithEmail(email: String, password: String) = firebase.signUpWithEmail(email, password)

    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> =
        google?.signInWithGoogle(idToken) ?: Result.failure(UnsupportedOperationException("Google Sign-In not available"))

    override suspend fun signOut() = firebase.signOut()

    override fun getCurrentUser(): AuthUser? = firebase.getCurrentUser()
}