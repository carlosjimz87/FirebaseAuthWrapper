package com.carlosjimz87.auth.data.datasource

import com.carlosjimz87.auth.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RealGoogleAuthDataSourceTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var authResult: AuthResult
    private lateinit var dataSource: RealGoogleAuthDataSource

    @Before
    fun setup() {
        firebaseAuth = mockk(relaxed = true)
        firebaseUser = mockk(relaxed = true)
        authResult = mockk(relaxed = true)
        dataSource = RealGoogleAuthDataSource(firebaseAuth)
    }

    @Test
    fun `signInWithGoogle handles timeout-like failure`() = runTest {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        val task = mockk<Task<AuthResult>>()
        coEvery { task.await() } throws Exception("timeout")

        every { firebaseAuth.signInWithCredential(any()) } returns task

        val result = dataSource.signInWithGoogle(Constants.SUCCESS_ID_TOKEN)

        assertTrue(result.isFailure)
        assertEquals("timeout", result.exceptionOrNull()?.message)
    }
}