package com.carlosjimz87.firebaseauthwrapper

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.carlosjimz87.auth.domain.repo.AuthRepository
import com.carlosjimz87.firebaseauthwrapper.fortesting.FakeAuthRepository
import com.carlosjimz87.firebaseauthwrapper.presentation.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module

class LoginFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var fakeAuthRepository: FakeAuthRepository

    @Before
    fun setup() {
        stopKoin()
        fakeAuthRepository = FakeAuthRepository().apply { simulateSuccess = true }
        startKoin {
            modules(module {
                single<AuthRepository> { fakeAuthRepository }
            })
        }
    }

    @Test
    fun loginSuccess_navigatesToMainContent() {
        composeTestRule.onNodeWithTag("EmailField").performTextInput("test@test.com")
        composeTestRule.onNodeWithTag("PasswordField").performTextInput("password")
        composeTestRule.onNodeWithTag("SignInButton").performClick()

        composeTestRule.waitUntil(3_000) {
            composeTestRule.onAllNodesWithTag("MainScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("MainScreen").assertIsDisplayed()
    }
}