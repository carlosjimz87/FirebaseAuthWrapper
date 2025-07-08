package com.carlosjimz87.auth.data.datasource

import com.carlosjimz87.auth.data.datasource.interfaces.FirebaseAuthDataSource
import com.carlosjimz87.auth.data.mapper.toDomain
import com.carlosjimz87.auth.domain.model.AuthUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class RealFirebaseAuthDataSource(private val auth: FirebaseAuth) : FirebaseAuthDataSource {

    override suspend fun signInWithEmail(email: String, password: String): Result<AuthUser> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
        auth.currentUser?.toDomain() ?: throw Exception("User not found")
    }

    override fun getCurrentUser(): AuthUser? = auth.currentUser?.toDomain()

    override suspend fun signOut() {
        auth.signOut()
    }
}