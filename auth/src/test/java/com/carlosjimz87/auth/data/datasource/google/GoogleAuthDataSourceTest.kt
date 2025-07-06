package com.carlosjimz87.auth.data.datasource.google


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
class GoogleAuthDataSourceTest {

    private lateinit var dataSource: GoogleAuthDataSource
    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firebaseUser = mockk<FirebaseUser>(relaxed = true)
    private val authResult = mockk<AuthResult>(relaxed = true)

    @Before
    fun setup() {
        dataSource = GoogleAuthDataSource(firebaseAuth)
    }

    @Test
    fun `signInWithGoogle returns success`() = runTest {
        every { firebaseAuth.currentUser } returns firebaseUser
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        val task = mockk<Task<AuthResult>>()
        coEvery { task.await() } returns authResult

        every { firebaseAuth.signInWithCredential(any()) } returns task
        every { firebaseUser.uid } returns "1"
        every { firebaseUser.email } returns "google@test.com"
        every { firebaseUser.displayName } returns "Google User"
        every { firebaseUser.photoUrl } returns null

        val result = dataSource.signInWithGoogle("fakeIdToken")

        assertTrue(result.isSuccess)
        assertEquals("google@test.com", result.getOrNull()?.email)
    }

    @Test
    fun `signInWithGoogle returns failure`() = runTest {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        val task = mockk<Task<AuthResult>>()
        coEvery { task.await() } throws Exception("Google login failed")

        every { firebaseAuth.signInWithCredential(any()) } returns task

        val result = dataSource.signInWithGoogle("fakeIdToken")

        assertTrue(result.isFailure)
    }
}