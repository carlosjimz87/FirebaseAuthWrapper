package com.carlosjimz87.auth.data.datasource.google

import com.carlosjimz87.auth.data.mapper.toDomain
import com.carlosjimz87.auth.domain.model.AuthUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthDataSource(private val auth: FirebaseAuth) {

    suspend fun signInWithGoogle(idToken: String): Result<AuthUser> = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
        auth.currentUser?.toDomain() ?: throw Exception("User not found")
    }
}