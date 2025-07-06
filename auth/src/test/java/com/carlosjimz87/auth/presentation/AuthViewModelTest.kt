package com.carlosjimz87.auth.presentation

import com.carlosjimz87.auth.data.repository.FakeAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AuthViewModel

    private lateinit var fakeAuthRepository: FakeAuthRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository()
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
        fakeAuthRepository.simulateSuccess = true
        viewModel.onEmailChanged("test@test.com")
        viewModel.onPasswordChanged("password")

        viewModel.emailLogin()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `login failure updates state with error`() = runTest {
        fakeAuthRepository.simulateSuccess = false
        viewModel.onEmailChanged("test@test.com")
        viewModel.onPasswordChanged("wrong")

        viewModel.emailLogin()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun `googleLogin success updates state to success`() = runTest {
        fakeAuthRepository.simulateSuccess = true
        viewModel.googleLogin("fakeIdToken")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `googleLogin failure updates state with error`() = runTest {
        fakeAuthRepository.simulateSuccess = false
        viewModel.googleLogin("fakeIdToken")
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }
}