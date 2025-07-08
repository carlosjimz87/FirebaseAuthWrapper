package com.carlosjimz87.auth.data.repository

import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.di.authModule
import com.carlosjimz87.auth.di.testAuthModule
import com.carlosjimz87.auth.domain.repo.AuthRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.java.KoinJavaComponent.getKoin

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        startKoin {
            modules(
                listOf(
                    authModule,      // production bindings
                    testAuthModule   // override fakes
                )
            )
        }

        repository = getKoin().get()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        stopKoin()
    }

    @Test
    fun `signInWithEmail returns success when credentials are valid`() = runTest {
        val result = repository.signInWithEmail(Constants.SUCCESS_EMAIL, Constants.SUCCESS_PASSWORD)

        assertTrue(result.isSuccess)
        assertEquals(Constants.SUCCESS_EMAIL, result.getOrNull()?.email)
    }

    @Test
    fun `signInWithEmail returns failure when credentials are invalid`() = runTest {
        val result = repository.signInWithEmail(Constants.FAILURE_EMAIL, Constants.FAILURE_PASSWORD)

        assertTrue(result.isFailure)
        assertEquals("Fake authentication failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `signInWithGoogle returns success when token is valid`() = runTest {
        val result = repository.signInWithGoogle(Constants.SUCCESS_ID_TOKEN)

        assertTrue(result.isSuccess)
        assertEquals(Constants.SUCCESS_ID_TOKEN, result.getOrNull()?.uid)
    }

    @Test
    fun `signInWithGoogle returns failure when token is invalid`() = runTest {
        val result = repository.signInWithGoogle(Constants.FAILURE_ID_TOKEN)

        assertTrue(result.isFailure)
        assertEquals("Fake Google authentication failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getCurrentUser returns user after login`() = runTest {
        repository.signInWithEmail(Constants.SUCCESS_EMAIL, Constants.SUCCESS_PASSWORD)

        val user = repository.getCurrentUser()
        assertEquals(Constants.SUCCESS_EMAIL, user?.email)
    }

    @Test
    fun `signOut clears current user`() = runTest {
        repository.signInWithEmail(Constants.SUCCESS_EMAIL, Constants.SUCCESS_PASSWORD)
        repository.signOut()

        val user = repository.getCurrentUser()
        assertNull(user)
    }
}