package com.example.musicevents.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.models.Theme
import com.example.musicevents.data.repositories.ThemeRepository
import com.example.musicevents.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ThemeState(val theme: Theme)

interface SettingsAction{
    fun logOut()
    fun changeTheme(theme: Theme)
}
class SettingsViewModel(
    private val userRepository: UserRepository,
    private val themeRepository: ThemeRepository
) : ViewModel(){

    val state = themeRepository.theme.map { ThemeState(theme = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ThemeState(Theme.System)
    )

    fun setTheme(theme: Theme) = viewModelScope.launch {
        themeRepository.setTheme(theme)
    }

    val actions = object: SettingsAction{
        override fun logOut() {
            viewModelScope.launch {
                userRepository.logOut()
            }
        }

        override fun changeTheme(theme: Theme){
            setTheme(theme)
        }
    }
}