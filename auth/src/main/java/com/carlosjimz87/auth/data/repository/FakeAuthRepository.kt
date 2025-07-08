package com.carlosjimz87.auth.data.repository

import com.carlosjimz87.auth.Constants.TEST_NAME
import com.carlosjimz87.auth.Constants.TEST_EMAIL
import com.carlosjimz87.auth.domain.model.AuthUser
import com.carlosjimz87.auth.domain.repo.AuthRepository

class FakeAuthRepository : AuthRepository {

    var simulateSuccess = true

    override suspend fun signInWithEmail(email: String, password: String) =
        if (simulateSuccess) Result.success(AuthUser("1", email, password, null))
        else Result.failure(Exception("Invalid credentials"))

    override suspend fun signUpWithEmail(email: String, password: String) =
        signInWithEmail(email, password)

    override suspend fun signInWithGoogle(idToken: String) =
        if (simulateSuccess) Result.success(
            AuthUser("2", TEST_EMAIL, TEST_NAME, null)
        )
        else Result.failure(Exception("Invalid Google credentials"))

    override suspend fun signOut() {}

    override fun getCurrentUser(): AuthUser? = null
}