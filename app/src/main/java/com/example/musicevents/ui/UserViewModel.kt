package com.example.musicevents.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface UserActions{
    fun changeUsername(name: String)
}

class UserViewModel(
    private val userRepository: UserRepository,
): ViewModel() {
    val actions = object: UserActions{
        override fun changeUsername(name: String) {
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.changeUsername(name)
            }
        }
    }
}