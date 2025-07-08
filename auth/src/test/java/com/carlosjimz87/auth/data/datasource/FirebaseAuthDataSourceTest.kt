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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseAuthDataSourceTest {

    private lateinit var dataSource: RealFirebaseAuthDataSource
    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firebaseUser = mockk<FirebaseUser>(relaxed = true)
    private val authResult = mockk<AuthResult>(relaxed = true)

    @Before
    fun setup() {
        dataSource = RealFirebaseAuthDataSource(firebaseAuth)
    }

    @Test
    fun `signInWithEmail returns success`() = runTest {
        every { firebaseAuth.currentUser } returns firebaseUser
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        val task = mockk<Task<AuthResult>>()
        coEvery { task.await() } returns authResult
        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns task
        every { firebaseUser.uid } returns "1"
        every { firebaseUser.email } returns Constants.TEST_EMAIL
        every { firebaseUser.displayName } returns "Test"
        every { firebaseUser.photoUrl } returns null

        val result = dataSource.signInWithEmail(Constants.TEST_EMAIL, Constants.TEST_PASSWORD)

        assertTrue(result.isSuccess)
        assertEquals(Constants.TEST_EMAIL, result.getOrNull()?.email)
    }

    @Test
    fun `signInWithEmail returns failure`() = runTest {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        val task = mockk<Task<AuthResult>>()
        coEvery { task.await() } throws Exception("Auth failed")
        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns task

        val result = dataSource.signInWithEmail(Constants.TEST_EMAIL, Constants.TEST_PASSWORD)

        assertTrue(result.isFailure)
    }
}