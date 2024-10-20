package com.semeprojects.secom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semeprojects.secom.data.preferences.Theme
import com.semeprojects.secom.data.preferences.ThemeDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeDataStoreRepository: ThemeDataStoreRepository
) : ViewModel() {

    private val _theme = MutableStateFlow(Theme.SYSTEM)
    val theme: StateFlow<Theme> = _theme.asStateFlow()

    init {
        viewModelScope.launch {
            themeDataStoreRepository.readTheme().collect {
                _theme.value = it
            }
        }
    }

    fun setTheme(newTheme: Theme) {
        viewModelScope.launch {
            themeDataStoreRepository.saveTheme(newTheme)
            _theme.value = newTheme
        }
    }
}