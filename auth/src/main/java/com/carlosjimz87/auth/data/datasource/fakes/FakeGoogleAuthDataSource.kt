package com.carlosjimz87.auth.data.datasource.fakes

import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.data.datasource.interfaces.GoogleAuthDataSource
import com.carlosjimz87.auth.domain.model.AuthUser

class FakeGoogleAuthDataSource() : GoogleAuthDataSource {

    private var fakeUser: AuthUser? = null

    override suspend fun signInWithGoogle(token: String): Result<AuthUser> {
        return if (token == Constants.FAILURE_ID_TOKEN) {
            Result.failure(Exception("Fake Google authentication failed"))
        } else {
            fakeUser = AuthUser(Constants.SUCCESS_ID_TOKEN, Constants.GOOGLE_USER_EMAIL, Constants.GOOGLE_USER_NAME, null)
            Result.success(fakeUser!!)
        }
    }
}