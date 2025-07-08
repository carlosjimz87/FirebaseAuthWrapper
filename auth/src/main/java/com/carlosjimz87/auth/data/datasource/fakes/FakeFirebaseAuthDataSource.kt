package com.carlosjimz87.auth.data.datasource.fakes

import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.data.datasource.interfaces.FirebaseAuthDataSource
import com.carlosjimz87.auth.domain.model.AuthUser

class FakeFirebaseAuthDataSource : FirebaseAuthDataSource {

    private var fakeUser: AuthUser? = null

    // Flags to simulate specific failure scenarios
    var shouldFailNextSignIn = false
    var shouldFailNextSignOut = false
    var shouldFailNextSignUp = false

    override suspend fun signInWithEmail(email: String, password: String): Result<AuthUser> {
        return when {
            shouldFailNextSignIn.also { shouldFailNextSignIn = false } ->
                Result.failure(Exception("Simulated Firebase sign-in failure"))

            email == Constants.SUCCESS_EMAIL && password == Constants.SUCCESS_PASSWORD -> {
                fakeUser = AuthUser("1", email, "Fake User", null)
                Result.success(fakeUser!!)
            }

            email == Constants.FAILURE_EMAIL && password == Constants.FAILURE_PASSWORD ->
                Result.failure(Exception("Authentication failed"))

            else ->
                Result.failure(Exception("Invalid credentials"))
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<AuthUser> {
        return when {
            shouldFailNextSignUp.also { shouldFailNextSignUp = false } ->
                Result.failure(Exception("Simulated sign-up failure"))

            email == Constants.FAILURE_EMAIL -> {
                Result.failure(Exception("Email already in use"))
            }

            else -> {
                fakeUser = AuthUser("newId", email, "New Fake User", null)
                Result.success(fakeUser!!)
            }
        }
    }

    override fun getCurrentUser(): AuthUser? {
        return fakeUser
    }

    override suspend fun signOut() {
        if (shouldFailNextSignOut.also { shouldFailNextSignOut = false }) {
            throw Exception("Simulated sign-out failure")
        }
        fakeUser = null
    }
}