package com.carlosjimz87.auth.data.datasource

import com.carlosjimz87.auth.data.mapper.toDomain
import com.carlosjimz87.auth.domain.model.AuthUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthDataSource(private val auth: FirebaseAuth) {

    suspend fun signInWithEmail(email: String, password: String): Result<AuthUser> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
        auth.currentUser?.toDomain() ?: throw Exception("User not found")
    }

    suspend fun signUpWithEmail(email: String, password: String): Result<AuthUser> = runCatching {
        auth.createUserWithEmailAndPassword(email, password).await()
        auth.currentUser?.toDomain() ?: throw Exception("User not found")
    }

    fun getCurrentUser(): AuthUser? = auth.currentUser?.toDomain()

    suspend fun signOut() { auth.signOut() }
}