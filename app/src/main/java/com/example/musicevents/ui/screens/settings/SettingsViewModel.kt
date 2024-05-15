package com.example.musicevents.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class Theme {Light, Dark, System}
data class ThemeState(val theme: Theme)

interface SettingsAction{
    fun logOut()
    fun changeTheme(theme: Theme)
}
class SettingsViewModel(
    private val userRepository: UserRepository
) : ViewModel(){

    private val _state = MutableStateFlow(ThemeState(Theme.System))
    val state = _state.asStateFlow()

    val actions = object: SettingsAction{
        override fun logOut() {
            viewModelScope.launch {
                userRepository.logOut()
            }
        }

        override fun changeTheme(theme: Theme) {
            _state.value = ThemeState(theme)
        }
    }
}