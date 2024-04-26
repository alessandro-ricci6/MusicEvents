package com.example.musicevents.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.repositories.UserRepository
import kotlinx.coroutines.launch

data class SettingsState(val username: String)

interface SettingsAction{
    fun logOut()
}
class SettingsViewModel(
    private val userRepository: UserRepository
) : ViewModel(){
    val actions = object: SettingsAction{
        override fun logOut() {
            viewModelScope.launch {
                userRepository.logOut()
            }
        }
    }
}