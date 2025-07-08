package com.carlosjimz87.auth.data.datasource.fakes

import com.carlosjimz87.auth.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class FakeFirebaseAuthDataSourceTest {

    private lateinit var dataSource: FakeFirebaseAuthDataSource

    @Before
    fun setup() {
        dataSource = FakeFirebaseAuthDataSource()
    }

    @Test
    fun `signInWithEmail returns success with valid credentials`() = runTest {
        val result = dataSource.signInWithEmail(Constants.SUCCESS_EMAIL, Constants.SUCCESS_PASSWORD)
        assertTrue(result.isSuccess)
        assertEquals(Constants.SUCCESS_EMAIL, result.getOrNull()?.email)
    }

    @Test
    fun `signInWithEmail returns failure with invalid credentials`() = runTest {
        val result = dataSource.signInWithEmail(Constants.FAILURE_EMAIL, Constants.FAILURE_PASSWORD)
        assertTrue(result.isFailure)
        assertEquals("Authentication failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `signInWithEmail returns failure when shouldFailNextSignIn is true`() = runTest {
        dataSource.shouldFailNextSignIn = true
        val result = dataSource.signInWithEmail(Constants.SUCCESS_EMAIL, Constants.SUCCESS_PASSWORD)
        assertTrue(result.isFailure)
        assertEquals("Simulated Firebase sign-in failure", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getCurrentUser returns the last signed-in user`() = runTest {
        dataSource.signInWithEmail(Constants.SUCCESS_EMAIL, Constants.SUCCESS_PASSWORD)
        val user = dataSource.getCurrentUser()
        assertNotNull(user)
        assertEquals(Constants.SUCCESS_EMAIL, user?.email)
    }

    @Test
    fun `signOut clears current user`() = runTest {
        dataSource.signInWithEmail(Constants.SUCCESS_EMAIL, Constants.SUCCESS_PASSWORD)
        dataSource.signOut()
        val user = dataSource.getCurrentUser()
        assertNull(user)
    }

    @Test
    fun `signOut fails when shouldFailNextSignOut is true`() = runTest {
        dataSource.signInWithEmail(Constants.SUCCESS_EMAIL, Constants.SUCCESS_PASSWORD)
        dataSource.shouldFailNextSignOut = true

        val exception = runCatching { dataSource.signOut() }.exceptionOrNull()
        assertNotNull(exception)
        assertEquals("Simulated sign-out failure", exception?.message)
    }

    @Test
    fun `signInWithEmail returns failure on valid email but wrong password`() = runTest {
        val result = dataSource.signInWithEmail(Constants.SUCCESS_EMAIL, "wrongpassword")
        assertTrue(result.isFailure)
        assertEquals("Invalid credentials", result.exceptionOrNull()?.message)
    }
}