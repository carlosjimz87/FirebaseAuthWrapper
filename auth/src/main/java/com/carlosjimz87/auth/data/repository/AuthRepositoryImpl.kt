package com.carlosjimz87.auth.data.repository

import com.carlosjimz87.auth.data.datasource.interfaces.FirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.interfaces.GoogleAuthDataSource
import com.carlosjimz87.auth.domain.model.AuthUser
import com.carlosjimz87.auth.domain.repo.AuthRepository

class AuthRepositoryImpl(
    private val firebase: FirebaseAuthDataSource,
    private val google: GoogleAuthDataSource? = null
) : AuthRepository {

    override suspend fun signInWithEmail(email: String, password: String): Result<AuthUser> {
        return firebase.signInWithEmail(email, password)
    }

    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> =
        google?.signInWithGoogle(idToken) ?: Result.failure(UnsupportedOperationException("Google Sign-In not available"))

    override suspend fun signOut() = firebase.signOut()

    override fun getCurrentUser(): AuthUser? = firebase.getCurrentUser()
}