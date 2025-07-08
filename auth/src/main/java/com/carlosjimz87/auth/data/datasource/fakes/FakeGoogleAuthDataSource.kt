package com.carlosjimz87.auth.data.datasource.fakes

import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.data.datasource.interfaces.GoogleAuthDataSource
import com.carlosjimz87.auth.domain.model.AuthUser

class FakeGoogleAuthDataSource : GoogleAuthDataSource {

    var shouldFailNextSignIn = false

    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> {
        return when {
            shouldFailNextSignIn.also { shouldFailNextSignIn = false } ->
                Result.failure(Exception("Simulated Google sign-in failure"))

            idToken == Constants.SUCCESS_ID_TOKEN -> {
                Result.success(AuthUser(Constants.SUCCESS_ID_TOKEN, Constants.GOOGLE_USER_EMAIL,
                    Constants.GOOGLE_USER_NAME, null))
            }

            idToken == Constants.FAILURE_ID_TOKEN ->
                Result.failure(Exception("Google ID token invalid"))

            else ->
                Result.failure(Exception("Unknown token"))
        }
    }
}