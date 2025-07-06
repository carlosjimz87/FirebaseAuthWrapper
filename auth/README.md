# Android Auth Module (Firebase Ready)

A clean, reusable Kotlin module for Firebase-based authentication in Android apps.  
Provides logic and state management, you fully control the UI.

## ✨ Features

✅ Email/Password Login  
✅ Google Sign-In ready  
✅ Clean Architecture  
✅ Koin-based Dependency Injection  
✅ You own the UI, we provide the logic

## ⚙️ Integration

1. Add JitPack to your root `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

2. In your build.gradle.kts:

```kotlin
dependencies {
    implementation("com.github.carlosjimz87:auth:1.0.0")
}
```

3.  Ensure your app:

-   Has google-services.json.
-   Initializes Firebase in Application.
-   Applies com.google.gms.google-services.

4.  Inject and use the needed classes:
   
- LoginViewModel
- AuthRepository

## 📋 License

Apache 2.0