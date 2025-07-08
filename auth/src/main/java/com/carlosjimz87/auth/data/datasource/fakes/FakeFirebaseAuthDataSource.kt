package com.carlosjimz87.auth.data.datasource.fakes

import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.data.datasource.interfaces.FirebaseAuthDataSource
import com.carlosjimz87.auth.domain.model.AuthUser

class FakeFirebaseAuthDataSource : FirebaseAuthDataSource {

    private var fakeUser: AuthUser? = null

    override suspend fun signInWithEmail(email: String, password: String): Result<AuthUser> {
        return when {
            email == Constants.SUCCESS_EMAIL && password == Constants.SUCCESS_PASSWORD -> {
                fakeUser = AuthUser("1", email, "Fake User", null)
                Result.success(fakeUser!!)
            }
            email == Constants.FAILURE_EMAIL && password == Constants.FAILURE_PASSWORD -> {
                Result.failure(Exception("Fake authentication failed"))
            }
            else -> {
                Result.failure(Exception("Invalid credentials"))
            }
        }
    }

    override fun getCurrentUser(): AuthUser? {
        return fakeUser
    }

    override suspend fun signOut() {
        fakeUser = null
    }
}