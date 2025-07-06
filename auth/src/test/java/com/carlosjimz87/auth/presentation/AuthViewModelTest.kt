package com.carlosjimz87.auth.presentation

import app.cash.turbine.test
import com.carlosjimz87.auth.domain.model.AuthUser
import com.carlosjimz87.auth.domain.repo.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Assert.*
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AuthViewModel

    private var simulateSuccess = true

    private val fakeAuthRepository = object : AuthRepository {
        override suspend fun signInWithEmail(email: String, password: String) =
            if (simulateSuccess) Result.success(AuthUser("1", email, "Test", null))
            else Result.failure(Exception("Invalid credentials"))

        override suspend fun signUpWithEmail(email: String, password: String) = signInWithEmail(email, password)

        override suspend fun signInWithGoogle(idToken: String) = signInWithEmail("google@test.com", "password")

        override suspend fun signOut() {}

        override fun getCurrentUser(): AuthUser? = null
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(fakeAuthRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEmailChanged updates email`() {
        viewModel.onEmailChanged("test@test.com")
        assertEquals("test@test.com", viewModel.uiState.value.email)
    }

    @Test
    fun `onPasswordChanged updates password`() {
        viewModel.onPasswordChanged("123456")
        assertEquals("123456", viewModel.uiState.value.password)
    }

    @Test
    fun `login success updates state to success`() = runTest {
        simulateSuccess = true
        viewModel.onEmailChanged("test@test.com")
        viewModel.onPasswordChanged("password")

        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `login failure updates state with error`() = runTest {
        simulateSuccess = false
        viewModel.onEmailChanged("test@test.com")
        viewModel.onPasswordChanged("wrong")

        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun `googleLogin success updates state to success`() = runTest {
        simulateSuccess = true
        viewModel.googleLogin("fakeIdToken")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `googleLogin failure updates state with error`() = runTest {
        simulateSuccess = false
        viewModel.googleLogin("fakeIdToken")
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }
}