package com.carlosjimz87.auth.data.repository

import com.carlosjimz87.auth.data.datasource.FirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.google.GoogleAuthDataSource
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
    private val firebaseDataSource = mockk<FirebaseAuthDataSource>()
    private val googleDataSource = mockk<GoogleAuthDataSource>()

    private val testUser = AuthUser("1", "test@test.com", "Test User", null)

    @Before
    fun setup() {
        repository = AuthRepositoryImpl(firebaseDataSource, googleDataSource)
    }

    @Test
    fun `signInWithEmail success returns user`() = runTest {
        coEvery { firebaseDataSource.signInWithEmail(any(), any()) } returns Result.success(testUser)

        val result = repository.signInWithEmail("test@test.com", "password")

        assertTrue(result.isSuccess)
        assertEquals(testUser, result.getOrNull())
    }

    @Test
    fun `signInWithEmail failure returns failure`() = runTest {
        coEvery { firebaseDataSource.signInWithEmail(any(), any()) } returns Result.failure(Exception("Error"))

        val result = repository.signInWithEmail("test@test.com", "password")

        assertTrue(result.isFailure)
    }

    @Test
    fun `signInWithGoogle success returns user`() = runTest {
        coEvery { googleDataSource.signInWithGoogle(any()) } returns Result.success(testUser)

        val result = repository.signInWithGoogle("fakeIdToken")

        assertTrue(result.isSuccess)
        assertEquals(testUser, result.getOrNull())
    }

    @Test
    fun `signInWithGoogle failure returns failure`() = runTest {
        coEvery { googleDataSource.signInWithGoogle(any()) } returns Result.failure(Exception("Error"))

        val result = repository.signInWithGoogle("fakeIdToken")

        assertTrue(result.isFailure)
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