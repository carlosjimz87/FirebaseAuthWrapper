package com.carlosjimz87.auth.domain.model

import android.net.Uri

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: Uri?
)