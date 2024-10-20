package com.semeprojects.secom.viewmodel

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


sealed class ProfileUIState {
    object Loading : ProfileUIState()
    data class Success(val user: User) : ProfileUIState()
    data class Error(val message: String) : ProfileUIState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: FirebaseUserRepository
): ViewModel() {

    private val _profileUiState = MutableStateFlow<ProfileUIState>(ProfileUIState.Loading)
    val profileUiState: StateFlow<ProfileUIState> = _profileUiState.asStateFlow()

    init {
        getProfile()
    }

    private fun getProfile(){
        viewModelScope.launch {
            _profileUiState.value = ProfileUIState.Loading
            val result = userRepository.getCurrentUser()
            if(result != null){
                _profileUiState.value = ProfileUIState.Success(result)
            } else {
                _profileUiState.value = ProfileUIState.Success(User(
                    displayName = "Guest User",
                    email = "No Email",
                    uid = "No UID",
                ))
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            _profileUiState.value = ProfileUIState.Loading
            val result = userRepository.signOut()
            if(result.isFailure){
                _profileUiState.value = ProfileUIState.Error("Error Happened $result")
            }
        }
    }
}