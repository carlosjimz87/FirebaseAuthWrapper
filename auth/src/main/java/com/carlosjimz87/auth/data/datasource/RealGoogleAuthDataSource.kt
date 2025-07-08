package com.carlosjimz87.auth.data.datasource

import com.carlosjimz87.auth.data.datasource.interfaces.GoogleAuthDataSource
import com.carlosjimz87.auth.data.mapper.toDomain
import com.carlosjimz87.auth.domain.model.AuthUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class RealGoogleAuthDataSource(private val auth: FirebaseAuth) : GoogleAuthDataSource{

    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
        auth.currentUser?.toDomain() ?: throw Exception("User not found")
    }
}