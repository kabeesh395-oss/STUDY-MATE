package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AuthUserModel
import com.example.data.repository.FirebaseAuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = FirebaseAuthRepository()

    private val _currentUser = MutableStateFlow<AuthUserModel?>(null)
    val currentUser: StateFlow<AuthUserModel?> = _currentUser.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _resetEmailSent = MutableStateFlow(false)
    val resetEmailSent: StateFlow<Boolean> = _resetEmailSent.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.authStateFlow.collect { firebaseUser ->
                if (firebaseUser != null) {
                    _isAuthenticated.value = true
                    loadUserProfile(firebaseUser)
                } else {
                    _isAuthenticated.value = false
                    _currentUser.value = null
                }
            }
        }
    }

    private fun loadUserProfile(firebaseUser: FirebaseUser) {
        viewModelScope.launch {
            // Attempt to load rich profile from Firestore, fallback to FirebaseAuth profile
            val firestoreUser = authRepository.fetchUserFromFirestore(firebaseUser.uid)
            if (firestoreUser != null) {
                _currentUser.value = firestoreUser
            } else {
                val fallbackUser = AuthUserModel(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: firebaseUser.email?.substringBefore("@") ?: "StudyMate User",
                    email = firebaseUser.email ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString(),
                    isGoogleUser = firebaseUser.providerData.any { it.providerId.contains("google") }
                )
                _currentUser.value = fallbackUser
                authRepository.saveUserToFirestore(fallbackUser)
            }
        }
    }

    fun loginWithEmail(email: String, password: String, onSuccess: () -> Unit = {}) {
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Please enter both email and password."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = authRepository.signInWithEmail(email, password)
            _isLoading.value = false

            result.onSuccess {
                _errorMessage.value = null
                onSuccess()
            }.onFailure { exception ->
                Log.e("AuthViewModel", "Login error", exception)
                _errorMessage.value = formatAuthError(exception)
            }
        }
    }

    fun signUpWithEmail(name: String, email: String, password: String, confirmPass: String, onSuccess: () -> Unit = {}) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Please fill in all fields."
            return
        }

        if (password != confirmPass) {
            _errorMessage.value = "Passwords do not match."
            return
        }

        if (password.length < 6) {
            _errorMessage.value = "Password must be at least 6 characters."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = authRepository.signUpWithEmail(email, password, name)
            _isLoading.value = false

            result.onSuccess {
                _errorMessage.value = null
                onSuccess()
            }.onFailure { exception ->
                Log.e("AuthViewModel", "SignUp error", exception)
                _errorMessage.value = formatAuthError(exception)
            }
        }
    }

    fun handleGoogleCredential(credential: AuthCredential, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = authRepository.signInWithCredential(credential)
            _isLoading.value = false

            result.onSuccess {
                _errorMessage.value = null
                onSuccess()
            }.onFailure { exception ->
                Log.e("AuthViewModel", "Google SignIn error", exception)
                _errorMessage.value = formatAuthError(exception)
            }
        }
    }

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _errorMessage.value = "Please enter your email address to reset password."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = authRepository.sendPasswordResetEmail(email)
            _isLoading.value = false

            result.onSuccess {
                _resetEmailSent.value = true
            }.onFailure { exception ->
                _errorMessage.value = formatAuthError(exception)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _currentUser.value = null
            _isAuthenticated.value = false
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun resetResetEmailState() {
        _resetEmailSent.value = false
    }

    private fun formatAuthError(e: Throwable): String {
        val msg = e.message ?: ""
        return when {
            msg.contains("Firebase Auth is unavailable", ignoreCase = true) ->
                "Firebase Auth is initializing or currently unreachable. Please check your internet connection or try again in a moment."
            msg.contains("The email address is badly formatted", ignoreCase = true) -> "Invalid email address format."
            msg.contains("The password is invalid", ignoreCase = true) || msg.contains("invalid-credential", ignoreCase = true) -> "Incorrect email or password."
            msg.contains("The email address is already in use", ignoreCase = true) -> "An account already exists with this email address."
            msg.contains("user-not-found", ignoreCase = true) -> "No account found with this email."
            msg.contains("network", ignoreCase = true) || msg.contains("UNAVAILABLE", ignoreCase = true) -> "Network error. Please check your internet connection."
            else -> msg.ifBlank { "Authentication failed. Please check your internet connection and credentials." }
        }
    }
}
