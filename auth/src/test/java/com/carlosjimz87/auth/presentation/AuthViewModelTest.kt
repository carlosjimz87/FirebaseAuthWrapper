package com.carlosjimz87.auth.presentation

import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.di.authModule
import com.carlosjimz87.auth.di.testAuthModule
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
                    authModule,       // módulo de producción
                    testAuthModule    // sobrescribe los data sources
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
    fun `onEmailChanged updates email`() {
        viewModel.onEmailChanged(Constants.TEST_EMAIL)
        assertEquals(Constants.TEST_EMAIL, viewModel.uiState.value.email)
    }

    @Test
    fun `onPasswordChanged updates password`() {
        viewModel.onPasswordChanged(Constants.TEST_PASSWORD)
        assertEquals(Constants.TEST_PASSWORD, viewModel.uiState.value.password)
    }

    @Test
    fun `login success updates state to success`() = runTest {
        viewModel.onEmailChanged(Constants.SUCCESS_EMAIL)
        viewModel.onPasswordChanged(Constants.SUCCESS_PASSWORD)

        viewModel.emailLogin()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `login failure updates state with error`() = runTest {
        viewModel.onEmailChanged(Constants.FAILURE_EMAIL)
        viewModel.onPasswordChanged(Constants.FAILURE_PASSWORD)

        viewModel.emailLogin()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun `googleLogin success updates state to success`() = runTest {
        viewModel.googleLogin(Constants.SUCCESS_ID_TOKEN)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `googleLogin failure updates state with error`() = runTest {
        viewModel.googleLogin(Constants.FAILURE_ID_TOKEN)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.success)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }
}