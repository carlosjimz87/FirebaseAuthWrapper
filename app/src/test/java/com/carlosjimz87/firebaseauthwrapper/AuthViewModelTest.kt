package com.carlosjimz87.firebaseauthwrapper

import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.presentation.AuthViewModel
import com.carlosjimz87.auth.data.repository.FakeAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var viewModel: AuthViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository()
        viewModel = AuthViewModel(fakeAuthRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `email login success updates state to success`() = runTest {
        fakeAuthRepository.simulateSuccess = true

        viewModel.onEmailChanged(Constants.TEST_EMAIL)
        viewModel.onPasswordChanged(Constants.TEST_PASS)
        viewModel.emailLogin()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `email login failure updates state with error`() = runTest {
        fakeAuthRepository.simulateSuccess = false

        viewModel.onEmailChanged(Constants.TEST_EMAIL)
        viewModel.onPasswordChanged(Constants.TEST_WRONG_PASS)
        viewModel.emailLogin()

        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun `google login success updates state to success`() = runTest {
        fakeAuthRepository.simulateSuccess = true

        viewModel.googleLogin("fakeIdToken")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `google login failure updates state with error`() = runTest {
        fakeAuthRepository.simulateSuccess = false

        viewModel.googleLogin("fakeIdToken")
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun `onEmailChanged updates email field`() {
        viewModel.onEmailChanged("new@email.com")
        assertEquals("new@email.com", viewModel.uiState.value.email)
    }

    @Test
    fun `onPasswordChanged updates password field`() {
        viewModel.onPasswordChanged("mypassword")
        assertEquals("mypassword", viewModel.uiState.value.password)
    }
}