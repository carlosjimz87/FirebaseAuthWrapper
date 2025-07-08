package com.carlosjimz87.auth.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
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
        every { firebaseUser.email } returns "test@test.com"
        every { firebaseUser.displayName } returns "Test"
        every { firebaseUser.photoUrl } returns null

        val result = dataSource.signInWithEmail("test@test.com", "password")

        assertTrue(result.isSuccess)
        assertEquals("test@test.com", result.getOrNull()?.email)
    }

    @Test
    fun `signInWithEmail returns failure`() = runTest {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        val task = mockk<Task<AuthResult>>()
        coEvery { task.await() } throws Exception("Auth failed")
        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns task

        val result = dataSource.signInWithEmail("test@test.com", "password")

        assertTrue(result.isFailure)
    }
}