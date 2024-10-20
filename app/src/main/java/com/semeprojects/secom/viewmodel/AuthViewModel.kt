package com.semeprojects.secom.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semeprojects.secom.data.network.model.User
import com.semeprojects.secom.data.network.repository.FirebaseUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object Loading : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: FirebaseUserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val emailError = mutableStateOf("")
    val passwordError = mutableStateOf("")
    val confirmPasswordError = mutableStateOf("")

    init {
        viewModelScope.launch {
            val currentUser = userRepository.getCurrentUser()
            _authState.value = if (currentUser != null) {
                AuthState.Authenticated(currentUser)
            } else {
                AuthState.Unauthenticated
            }
            _user.value = User("1", "John Doe", "john.doe@example.com")
        }
    }

    fun validateEmail(email: String) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.value = "Invalid email format"
        } else {
            emailError.value = ""
        }
    }

    fun validatePassword(password: String) {
        if (password.length < 6) {
            passwordError.value = "Password must be at least 6 characters"
        } else {
            passwordError.value = ""
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            confirmPasswordError.value = "Passwords do not match"
        } else {
            confirmPasswordError.value = ""
        }
    }

    fun signUpWithEmailAndPassword(email: String, password: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.signUpWithEmailAndPassword(email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.Authenticated(result.getOrNull()!!)
            } else {
                _authState.value = AuthState.Unauthenticated
            }
            onComplete(result.isSuccess)
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.signInWithEmailAndPassword(email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.Authenticated(result.getOrNull()!!)
            } else {
                _authState.value = AuthState.Unauthenticated
            }
            onComplete(result.isSuccess)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.signOut()
            if(result.isSuccess){
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

}