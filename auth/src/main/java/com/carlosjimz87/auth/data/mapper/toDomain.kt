package com.carlosjimz87.auth.data.mapper

import com.carlosjimz87.auth.domain.model.AuthUser
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomain() = AuthUser(
    uid = uid,
    email = email,
    displayName = displayName,
    photoUrl = photoUrl
)