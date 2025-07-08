package com.carlosjimz87.firebaseauthwrapper

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.carlosjimz87.auth.Constants
import com.carlosjimz87.firebaseauthwrapper.presentation.MainActivity
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        val app = ApplicationProvider.getApplicationContext<TestApplication>()
        println(">> App class used: ${app::class.java.name}")
        assertTrue(app is TestApplication)
    }

    @Test
    fun loginSuccess_navigatesToMainContent() {
        composeTestRule.onNodeWithTag("EmailField").performTextInput(Constants.TEST_EMAIL)
        composeTestRule.onNodeWithTag("PasswordField").performTextInput(Constants.TEST_PASS)
        composeTestRule.onNodeWithTag("SignInButton").performClick()

        composeTestRule.waitUntil(3_000) {
            composeTestRule.onAllNodesWithTag("MainScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("MainScreen").assertIsDisplayed()
    }

    @Test
    fun loginFailure_showsError() {
        composeTestRule.onNodeWithTag("EmailField").performTextInput(Constants.TEST_EMAIL)
        composeTestRule.onNodeWithTag("PasswordField").performTextInput(Constants.TEST_PASS)
        composeTestRule.onNodeWithTag("SignInButton").performClick()

        composeTestRule.onNodeWithTag("ErrorText").assertIsDisplayed()
    }
}