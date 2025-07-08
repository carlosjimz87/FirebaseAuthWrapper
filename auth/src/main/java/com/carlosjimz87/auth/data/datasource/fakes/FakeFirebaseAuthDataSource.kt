package com.carlosjimz87.auth.data.datasource.fakes

import com.carlosjimz87.auth.data.datasource.interfaces.FirebaseAuthDataSource
import com.carlosjimz87.auth.domain.model.AuthUser

class FakeFirebaseAuthDataSource : FirebaseAuthDataSource {

    private var fakeUser: AuthUser? = null
    private var shouldFail = false

    override suspend fun signInWithEmail(email: String, password: String): Result<AuthUser> {
        return if (shouldFail) {
            Result.failure(Exception("Fake authentication failed"))
        } else {
            fakeUser = AuthUser("1", email, "Fake User", null)
            Result.success(fakeUser!!)
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<AuthUser> {
        return if (shouldFail) {
            Result.failure(Exception("Fake sign-up failed"))
        } else {
            fakeUser = AuthUser("2", email, "New Fake User", null)
            Result.success(fakeUser!!)
        }
    }

    override fun getCurrentUser(): AuthUser? {
        return fakeUser
    }

    override suspend fun signOut() {
        fakeUser = null
    }
}