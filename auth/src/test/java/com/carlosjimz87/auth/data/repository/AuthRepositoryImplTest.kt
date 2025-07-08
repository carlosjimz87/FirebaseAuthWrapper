package com.carlosjimz87.auth.data.repository

import com.carlosjimz87.auth.data.datasource.RealFirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.RealGoogleAuthDataSource
import com.carlosjimz87.auth.domain.model.AuthUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import io.mockk.*

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepositoryImpl
    private val firebaseDataSource = mockk<RealFirebaseAuthDataSource>()
    private val googleDataSource = mockk<RealGoogleAuthDataSource>()

    private val testUser = AuthUser("1", "test@test.com", "Test User", null)

    @Before
    fun setup() {
        repository = AuthRepositoryImpl(firebaseDataSource, googleDataSource)
    }

    @Test
    fun `signInWithEmail returns success when data source succeeds`() = runTest {
        val expectedUser = AuthUser("1", "test@test.com", "Test User", null)
        coEvery { firebaseDataSource.signInWithEmail(any(), any()) } returns Result.success(expectedUser)

        val result = repository.signInWithEmail("test@test.com", "password")

        assertTrue(result.isSuccess)
        assertEquals(expectedUser, result.getOrNull())
    }

    @Test
    fun `signInWithEmail returns failure when data source fails`() = runTest {
        coEvery { firebaseDataSource.signInWithEmail(any(), any()) } returns Result.failure(Exception("Auth error"))

        val result = repository.signInWithEmail("test@test.com", "password")

        assertTrue(result.isFailure)
        assertEquals("Auth error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `signInWithGoogle returns success when data source succeeds`() = runTest {
        val expectedUser = AuthUser("1", "test@test.com", "Test User", null)
        coEvery { googleDataSource.signInWithGoogle(any()) } returns Result.success(expectedUser)

        val result = repository.signInWithGoogle("fakeIdToken")

        assertTrue(result.isSuccess)
        assertEquals(expectedUser, result.getOrNull())
    }

    @Test
    fun `signInWithGoogle returns failure when data source fails`() = runTest {
        coEvery { googleDataSource.signInWithGoogle(any()) } returns Result.failure(Exception("Google auth error"))

        val result = repository.signInWithGoogle("fakeIdToken")

        assertTrue(result.isFailure)
        assertEquals("Google auth error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getCurrentUser returns user`() {
        every { firebaseDataSource.getCurrentUser() } returns testUser

        val result = repository.getCurrentUser()

        assertEquals(testUser, result)
    }

    @Test
    fun `signOut calls firebase signOut`() = runTest {
        coEvery { firebaseDataSource.signOut() } just Runs

        repository.signOut()

        coVerify(exactly = 1) { firebaseDataSource.signOut() }
    }
}