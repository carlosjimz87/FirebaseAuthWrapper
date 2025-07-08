package com.carlosjimz87.auth.data.repository

import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.data.datasource.fakes.FakeFirebaseAuthDataSource
import com.carlosjimz87.auth.data.datasource.interfaces.FirebaseAuthDataSource
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
    fun `signUpWithEmail returns success when new user is registered`() = runTest {
        val result = repository.signUpWithEmail(Constants.NEW_USER_EMAIL, Constants.NEW_USER_PASSWORD)

        assertTrue(result.isSuccess)
        assertEquals(Constants.NEW_USER_EMAIL, result.getOrNull()?.email)
    }

    @Test
    fun `signUpWithEmail returns failure when sign-up is simulated to fail`() = runTest {
        // Setup: enable simulated failure
        val fakeAuthDataSource = getKoin().get<FirebaseAuthDataSource>() as FakeFirebaseAuthDataSource
        fakeAuthDataSource.shouldFailNextSignUp = true

        val result = repository.signUpWithEmail(Constants.NEW_USER_EMAIL, Constants.NEW_USER_PASSWORD)

        assertTrue(result.isFailure)
        assertEquals("Simulated sign-up failure", result.exceptionOrNull()?.message)
    }

    @Test
    fun `signUpWithEmail returns failure when email is already used`() = runTest {
        // Simulate that email is already taken (if your Fake handles this)
        val result = repository.signUpWithEmail(Constants.FAILURE_EMAIL, Constants.FAILURE_PASSWORD)

        assertTrue(result.isFailure)
        assertEquals("Email already in use", result.exceptionOrNull()?.message)
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
        assertEquals("Authentication failed", result.exceptionOrNull()?.message)
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
        assertEquals("Google ID token invalid", result.exceptionOrNull()?.message)
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

    @Test
    fun `signOut without login does not crash or affect state`() = runTest {
        repository.signOut()

        val user = repository.getCurrentUser()
        assertNull(user)
    }
}