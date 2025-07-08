# 🔐 Firebase Auth Module for Android (Email & Google Sign-In)

A clean, testable, and fully modular Firebase Authentication layer for Android apps using **Koin**, **Jetpack Compose (optional)**, and **Kotlin Coroutines**.

---

## 📦 Features

- ✅ Sign in with Email & Password  
- ✅ Sign up with Email & Password  
- ✅ Sign in with Google ID Token  
- ✅ Sign out and retrieve current user  
- ✅ Strong `Result<AuthUser>`-based API to avoid exception leaks  
- ✅ Fully unit-tested real and fake data sources  
- ✅ Easily swappable `Fake` vs `Real` data sources for tests  
- ✅ Koin-friendly dependency injection  
- ✅ Easily integrable in any modern Android project

---

## 📂 Module Structure

auth/  
├── data/  
│   └── datasource/  
│       ├── FirebaseAuthDataSource.kt  
│       ├── GoogleAuthDataSource.kt  
│       ├── FakeFirebaseAuthDataSource.kt  
│       └── FakeGoogleAuthDataSource.kt  
├── domain/  
│   ├── model/AuthUser.kt  
│   └── repository/AuthRepository.kt  
├── presentation/  
│   └── AuthViewModel.kt  
├── di/  
│   └── AuthModule.kt  
└── test/  
    ├── FirebaseAuthDataSourceTest.kt  
    ├── GoogleAuthDataSourceTest.kt  
    ├── AuthRepositoryImplTest.kt  
    └── AuthViewModelTest.kt  

---

## 🧪 Test Coverage

Tested modules:

- ✅ `FirebaseAuthDataSource` (real + fake)  
- ✅ `GoogleAuthDataSource` (real + fake)  
- ✅ `AuthRepositoryImpl`  
- ✅ `AuthViewModel`  

Edge cases covered:

- Invalid credentials  
- Internal simulated failures  
- Invalid or missing Google tokens  
- `signOut()` before/after login  
- User not found after Firebase success  
- Timeouts and exception propagation  

---

## 🚀 Quick Integration

### 1. Add the Koin module to your app

```
startKoin {
    modules(authModule)
}

```

### 2. Inject your ViewModel

```
val viewModel: AuthViewModel = get()
```

### 3. Use the exposed API

```
viewModel.onEmailChanged("your@email.com")  
viewModel.onPasswordChanged("securePassword123")  
viewModel.emailLogin() // or viewModel.googleLogin(idToken)
```

---

## 👤 AuthUser Data Model

```
data class AuthUser(
    val uid: String,
    val email: String,
    val displayName: String?,
    val photoUrl: String?
)
```

---

## 🧪 Running Tests

To run all tests:

```
./gradlew :auth:test
```

All data sources can be tested independently. Fakes simulate common and edge-case scenarios.

---

## 🔄 Switching to Fake Data Sources

To override the real data sources for testing:

```
val testAuthModule = module {
    single<FirebaseAuthDataSource> { FakeFirebaseAuthDataSource() }
    single<GoogleAuthDataSource> { FakeGoogleAuthDataSource() }
}
```

Then use:

```
startKoin {
    modules(authModule, testAuthModule)
}
```

---

## 📄 License

MIT License.  
You’re free to use, modify, and distribute it in personal or commercial projects.

---

## 🧠 TODO / Future Work

- [ ] Support for Apple and Phone authentication  
- [ ] Multi-language error messaging  
- [ ] UI module with Jetpack Compose templates  
- [ ] Publish to Maven or Jitpack.
