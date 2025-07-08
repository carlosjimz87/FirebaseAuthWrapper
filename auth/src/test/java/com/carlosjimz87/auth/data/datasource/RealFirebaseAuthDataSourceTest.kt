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
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RealFirebaseAuthDataSourceTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var authResult: AuthResult
    private lateinit var dataSource: RealFirebaseAuthDataSource

    @Before
    fun setup() {
        firebaseAuth = mockk(relaxed = true)
        firebaseUser = mockk(relaxed = true)
        authResult = mockk(relaxed = true)
        dataSource = RealFirebaseAuthDataSource(firebaseAuth)
    }

    @Test
    fun `signInWithEmail returns success when Firebase returns user`() = runTest {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        val task = mockk<Task<AuthResult>>()

        coEvery { task.await() } returns authResult
        every { firebaseAuth.signInWithEmailAndPassword(Constants.TEST_EMAIL, Constants.TEST_PASSWORD) } returns task
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns Constants.TEST_ID
        every { firebaseUser.email } returns Constants.TEST_EMAIL
        every { firebaseUser.displayName } returns Constants.TEST_NAME
        every { firebaseUser.photoUrl } returns null

        val result = dataSource.signInWithEmail(Constants.TEST_EMAIL, Constants.TEST_PASSWORD)

        assertTrue(result.isSuccess)
        assertEquals(Constants.TEST_EMAIL, result.getOrNull()?.email)
    }

    @Test
    fun `signInWithEmail returns failure when currentUser is null`() = runTest {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        val task = mockk<Task<AuthResult>>()

        coEvery { task.await() } returns authResult
        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns task
        every { firebaseAuth.currentUser } returns null

        val result = dataSource.signInWithEmail("any@test.com", "any")

        assertTrue(result.isFailure)
        assertEquals("User not found", result.exceptionOrNull()?.message)
    }

    @Test
    fun `signInWithEmail returns failure when await throws exception`() = runTest {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        val task = mockk<Task<AuthResult>>()

        coEvery { task.await() } throws Exception("Network error")
        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns task

        val result = dataSource.signInWithEmail(Constants.FAILURE_EMAIL, Constants.FAILURE_PASSWORD)

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getCurrentUser returns AuthUser when currentUser is not null`() {
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns Constants.TEST_ID
        every { firebaseUser.email } returns Constants.TEST_EMAIL
        every { firebaseUser.displayName } returns Constants.TEST_NAME
        every { firebaseUser.photoUrl } returns null

        val user = dataSource.getCurrentUser()

        assertEquals(Constants.TEST_EMAIL, user?.email)
        assertEquals(Constants.TEST_NAME, user?.displayName)
    }

    @Test
    fun `getCurrentUser returns null when currentUser is null`() {
        every { firebaseAuth.currentUser } returns null

        val user = dataSource.getCurrentUser()

        assertEquals(null, user)
    }
}