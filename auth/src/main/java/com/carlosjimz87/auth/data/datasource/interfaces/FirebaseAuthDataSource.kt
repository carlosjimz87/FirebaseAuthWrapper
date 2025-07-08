package com.carlosjimz87.auth.data.datasource.interfaces

import com.carlosjimz87.auth.domain.model.AuthUser

interface FirebaseAuthDataSource {

    suspend fun signInWithEmail(email: String, password: String): Result<AuthUser>

    suspend fun signUpWithEmail(email: String, password: String): Result<AuthUser>

    fun getCurrentUser(): AuthUser?

    suspend fun signOut()
}