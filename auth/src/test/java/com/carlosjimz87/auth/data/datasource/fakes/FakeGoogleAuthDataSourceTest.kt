package com.carlosjimz87.auth.data.datasource.fakes

import com.carlosjimz87.auth.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FakeGoogleAuthDataSourceTest {

    private lateinit var dataSource: FakeGoogleAuthDataSource

    @Before
    fun setup() {
        dataSource = FakeGoogleAuthDataSource()
    }

    @Test
    fun `signInWithGoogle returns success with valid token`() = runTest {
        val result = dataSource.signInWithGoogle(Constants.SUCCESS_ID_TOKEN)
        assertTrue(result.isSuccess)
        assertEquals(Constants.GOOGLE_USER_EMAIL, result.getOrNull()?.email)
    }

    @Test
    fun `signInWithGoogle returns failure with invalid token`() = runTest {
        val result = dataSource.signInWithGoogle(Constants.FAILURE_ID_TOKEN)
        assertTrue(result.isFailure)
        assertEquals("Google ID token invalid", result.exceptionOrNull()?.message)
    }

    @Test
    fun `signInWithGoogle returns failure when shouldFailNextSignIn is true`() = runTest {
        dataSource.shouldFailNextSignIn = true
        val result = dataSource.signInWithGoogle(Constants.SUCCESS_ID_TOKEN)
        assertTrue(result.isFailure)
        assertEquals("Simulated Google sign-in failure", result.exceptionOrNull()?.message)
    }


    @Test
    fun `retry signInWithGoogle with valid token after failure`() = runTest {
        var result = dataSource.signInWithGoogle(Constants.FAILURE_ID_TOKEN)
        assertTrue(result.isFailure)

        result = dataSource.signInWithGoogle(Constants.SUCCESS_ID_TOKEN)
        assertTrue(result.isSuccess)
    }
}
