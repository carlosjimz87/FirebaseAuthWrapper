package com.carlosjimz87.auth.data.datasource.interfaces

import com.carlosjimz87.auth.domain.model.AuthUser

interface GoogleAuthDataSource {
    suspend fun signInWithGoogle(idToken: String): Result<AuthUser>
}