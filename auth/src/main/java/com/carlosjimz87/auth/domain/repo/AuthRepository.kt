package com.carlosjimz87.auth.domain.repo

import com.carlosjimz87.auth.domain.model.AuthUser

interface AuthRepository {
    suspend fun signInWithEmail(email: String, password: String): Result<AuthUser>
    suspend fun signUpWithEmail(email: String, password: String): Result<AuthUser>
    suspend fun signInWithGoogle(idToken: String): Result<AuthUser>
    suspend fun signOut()
    fun getCurrentUser(): AuthUser?
}