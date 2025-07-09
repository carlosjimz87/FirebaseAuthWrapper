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

        // Paso 1: Login fallido
        println("✅ Paso 1: Probando login fallido con credenciales incorrectas...")
        rule.onNodeWithTag("EmailField").performTextClearance()
        rule.onNodeWithTag("PasswordField").performTextClearance()
        rule.onNodeWithTag("EmailField").performTextInput(Constants.FAILURE_EMAIL)
        rule.onNodeWithTag("PasswordField").performTextInput(Constants.FAILURE_PASSWORD)
        rule.onNodeWithTag("SignInButton").performClick()
        rule.onNodeWithTag("ErrorText").assertIsDisplayed()
        println("✅ Login fallido detectado correctamente.")

        // Paso 2: Login exitoso
        println("✅ Paso 2: Probando login exitoso con credenciales correctas...")
        rule.onNodeWithTag("EmailField").performTextClearance()
        rule.onNodeWithTag("PasswordField").performTextClearance()
        rule.onNodeWithTag("EmailField").performTextInput(Constants.SUCCESS_EMAIL)
        rule.onNodeWithTag("PasswordField").performTextInput(Constants.SUCCESS_PASSWORD)
        rule.onNodeWithTag("SignInButton").performClick()
        rule.waitUntilNodeCount(tag = "MainScreen", count = 1)
        rule.onNodeWithTag("MainScreen").assertIsDisplayed()
        println("✅ Login correcto y navegación exitosa a MainScreen.")
        rule.waitForIdle()

        // Paso 3: Logout
        println("✅ Paso 3: Probando logout desde la MainScreen...")
        rule.onNodeWithTag("LogoutButton").performClick()
        println("✅ Logout realizado correctamente.")

        // Paso 4: Selección de token válido
        println("✅ Paso 4: Seleccionando token válido para login con Google...")
        rule.onNodeWithTag("SelectTokenButton").performClick()
        rule.onNodeWithText("Valid Token").performClick()
        rule.onNodeWithTag("SelectedTokenBadge").assertIsDisplayed()
        println("✅ Token seleccionado correctamente.")

        // Paso 5: Login con Google
        println("✅ Paso 5: Probando login con Google...")
        rule.onNodeWithTag("GoogleSignInButton").performClick()
        rule.waitUntilNodeCount(tag = "MainScreen", count = 1)
        rule.onNodeWithTag("MainScreen").assertIsDisplayed()
        println("✅ Login con Google exitoso y navegación a MainScreen.")
    }

    // Helper reutilizable
    private fun AndroidComposeTestRule<*, *>.waitUntilNodeCount(tag: String, count: Int) {
        waitUntil(3_000) {
            onAllNodesWithTag(tag).fetchSemanticsNodes().size == count
        }
    }
}