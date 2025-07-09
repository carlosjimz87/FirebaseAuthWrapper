package com.carlosjimz87.firebaseauthwrapper

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.di.authModule
import com.carlosjimz87.auth.presentation.AuthViewModel
import com.carlosjimz87.firebaseauthwrapper.di.TestAuthViewModel
import com.carlosjimz87.firebaseauthwrapper.di.testAuthModule
import com.carlosjimz87.firebaseauthwrapper.presentation.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.java.KoinJavaComponent.getKoin

@OptIn(ExperimentalTestApi::class)
class LoginFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        (getKoin().get<AuthViewModel>() as? TestAuthViewModel)?.reset()
    }

    @Test
    fun fullLoginFlow_worksAsExpected_withGoogleTokenSelection() {
        val rule = composeTestRule

        // Step 1: Failed login
        println("✅ Step 1: Testing failed login with invalid credentials...")
        rule.onNodeWithTag("EmailField").performTextClearance()
        rule.onNodeWithTag("PasswordField").performTextClearance()
        rule.onNodeWithTag("EmailField").performTextInput(Constants.FAILURE_EMAIL)
        rule.onNodeWithTag("PasswordField").performTextInput(Constants.FAILURE_PASSWORD)
        rule.onNodeWithTag("SignInButton").performClick()
        rule.onNodeWithTag("ErrorText").assertIsDisplayed()
        println("✅ Failed login correctly detected.")

        // Step 2: Successful login
        println("✅ Step 2: Testing successful login with valid credentials...")
        rule.onNodeWithTag("EmailField").performTextClearance()
        rule.onNodeWithTag("PasswordField").performTextClearance()
        rule.onNodeWithTag("EmailField").performTextInput(Constants.SUCCESS_EMAIL)
        rule.onNodeWithTag("PasswordField").performTextInput(Constants.SUCCESS_PASSWORD)
        rule.onNodeWithTag("SignInButton").performClick()
        rule.waitUntilNodeCount(tag = "MainScreen", count = 1)
        rule.onNodeWithTag("MainScreen").assertIsDisplayed()
        println("✅ Successful login and navigation to MainScreen.")
        rule.waitForIdle()

        // Step 3: Logout
        println("✅ Step 3: Testing logout from MainScreen...")
        rule.onNodeWithTag("LogoutButton").performClick()
        println("✅ Logout successfully executed.")

        // Step 4: Token selection for Google login
        println("✅ Step 4: Selecting valid token for Google login...")
        rule.onNodeWithTag("SelectTokenButton").performClick()
        rule.onNodeWithText("Valid Token").performClick()
        rule.onNodeWithTag("SelectedTokenBadge").assertIsDisplayed()
        println("✅ Token selected successfully.")

        // Step 5: Google login
        println("✅ Step 5: Testing Google login...")
        rule.onNodeWithTag("GoogleSignInButton").performClick()
        rule.waitUntilNodeCount(tag = "MainScreen", count = 1)
        rule.onNodeWithTag("MainScreen").assertIsDisplayed()
        println("✅ Google login successful and navigation to MainScreen.")
    }

    // Helper reutilizable
    private fun AndroidComposeTestRule<*, *>.waitUntilNodeCount(tag: String, count: Int) {
        waitUntil(3_000) {
            onAllNodesWithTag(tag).fetchSemanticsNodes().size == count
        }
    }
}