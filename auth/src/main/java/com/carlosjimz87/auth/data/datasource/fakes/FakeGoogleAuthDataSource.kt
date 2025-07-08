package com.carlosjimz87.auth.data.datasource.fakes

import com.carlosjimz87.auth.data.datasource.interfaces.GoogleAuthDataSource
import com.carlosjimz87.auth.domain.model.AuthUser

class FakeGoogleAuthDataSource() : GoogleAuthDataSource {

    private var shouldFail = false
    private var fakeUser: AuthUser? = null

    override suspend fun signInWithGoogle(token: String): Result<AuthUser> {
        return if (shouldFail) {
            Result.failure(Exception("Fake Google authentication failed"))
        } else {
            fakeUser = AuthUser("1", "google@test.com", "Google User", null)
            Result.success(fakeUser!!)
        }
    }
}