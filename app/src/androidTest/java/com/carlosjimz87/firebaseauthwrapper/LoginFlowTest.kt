package com.carlosjimz87.firebaseauthwrapper

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.presentation.AuthViewModel
import com.carlosjimz87.firebaseauthwrapper.presentation.MainActivity
import com.carlosjimz87.firebaseauthwrapper.scenario.TestAuthViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.java.KoinJavaComponent.getKoin

class LoginFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        composeTestRule.activityRule.scenario.recreate()
        (getKoin().get<AuthViewModel>() as? TestAuthViewModel)?.reset()
    }

    @Test
    fun loginSuccess_navigatesToMainContent() {

        composeTestRule.onNodeWithTag("EmailField").performTextInput(Constants.SUCCESS_EMAIL)
        composeTestRule.onNodeWithTag("PasswordField").performTextInput(Constants.SUCCESS_PASSWORD)
        composeTestRule.onNodeWithTag("SignInButton").performClick()

        composeTestRule.waitUntil(3_000) {
            composeTestRule.onAllNodesWithTag("MainScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("MainScreen").assertIsDisplayed()
    }

    @Test
    fun loginFailure_showsError() {

        composeTestRule.onNodeWithTag("EmailField").performTextInput(Constants.FAILURE_EMAIL)
        composeTestRule.onNodeWithTag("PasswordField").performTextInput(Constants.FAILURE_PASSWORD)
        composeTestRule.onNodeWithTag("SignInButton").performClick()

        composeTestRule.waitUntil(3_000) {
            composeTestRule.onAllNodesWithTag("ErrorText").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("ErrorText").assertIsDisplayed()
    }
}