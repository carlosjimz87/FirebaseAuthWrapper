package com.carlosjimz87.auth.presentation

import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.di.authModule
import com.carlosjimz87.auth.di.testAuthModule
import com.carlosjimz87.auth.domain.model.AuthUiEvent
import com.carlosjimz87.auth.domain.repo.AuthRepository
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
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.java.KoinJavaComponent.getKoin

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        startKoin {
            modules(
                listOf(
                    authModule,       // production module
                    testAuthModule    // overwrite datasources for testing
                )
            )
        }

        viewModel = getKoin().get()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        stopKoin()
    }

    @Test
    fun `EmailChanged updates email`() {
        viewModel.onEvent(AuthUiEvent.EmailChanged(Constants.TEST_EMAIL))
        assertEquals(Constants.TEST_EMAIL, viewModel.uiState.value.form.email)
    }

    @Test
    fun `PasswordChanged updates password`() {
        viewModel.onEvent(AuthUiEvent.PasswordChanged(Constants.TEST_PASSWORD))
        assertEquals(Constants.TEST_PASSWORD, viewModel.uiState.value.form.password)
    }

    @Test
    fun `SubmitLogin success updates state to success`() = runTest {
        viewModel.onEvent(AuthUiEvent.EmailChanged(Constants.SUCCESS_EMAIL))
        viewModel.onEvent(AuthUiEvent.PasswordChanged(Constants.SUCCESS_PASSWORD))

        viewModel.onEvent(AuthUiEvent.SubmitLogin)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `SubmitLogin failure updates state with error`() = runTest {
        viewModel.onEvent(AuthUiEvent.EmailChanged(Constants.FAILURE_EMAIL))
        viewModel.onEvent(AuthUiEvent.PasswordChanged(Constants.FAILURE_PASSWORD))

        viewModel.onEvent(AuthUiEvent.SubmitLogin)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun `GoogleLogin success updates state to success`() = runTest {
        viewModel.onEvent(AuthUiEvent.GoogleLogin(Constants.SUCCESS_ID_TOKEN))
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `GoogleLogin failure updates state with error`() = runTest {
        viewModel.onEvent(AuthUiEvent.GoogleLogin(Constants.FAILURE_ID_TOKEN))
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun `Logout resets user state`() = runTest {
        viewModel.onEvent(AuthUiEvent.EmailChanged(Constants.SUCCESS_EMAIL))
        viewModel.onEvent(AuthUiEvent.PasswordChanged(Constants.SUCCESS_PASSWORD))
        viewModel.onEvent(AuthUiEvent.SubmitLogin)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AuthUiEvent.Logout)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        val authRepository: AuthRepository = getKoin().get()

        assertFalse(state.success)
        assertNull(authRepository.getCurrentUser())
    }

    @Test
    fun `Retry login after failure updates state to success`() = runTest {
        viewModel.onEvent(AuthUiEvent.EmailChanged(Constants.FAILURE_EMAIL))
        viewModel.onEvent(AuthUiEvent.PasswordChanged(Constants.FAILURE_PASSWORD))
        viewModel.onEvent(AuthUiEvent.SubmitLogin)
        testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.success)

        viewModel.onEvent(AuthUiEvent.EmailChanged(Constants.SUCCESS_EMAIL))
        viewModel.onEvent(AuthUiEvent.PasswordChanged(Constants.SUCCESS_PASSWORD))
        viewModel.onEvent(AuthUiEvent.SubmitLogin)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
    }

    @Test
    fun `Logout without prior login does not crash or change state`() = runTest {
        viewModel.onEvent(AuthUiEvent.Logout)
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertFalse(state.success)
    }
}