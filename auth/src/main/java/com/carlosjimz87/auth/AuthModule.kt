package com.carlosjimz87.auth

import org.koin.dsl.module
import com.google.firebase.auth.FirebaseAuth

val authModule = module {

    single { FirebaseAuth.getInstance() }
}