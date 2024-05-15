package com.example.musicevents.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

interface UserActions{
    fun changeUsername(name: String)
}

class UserViewModel(
    private val userRepository: UserRepository,
): ViewModel() {

    var userId = userRepository.id.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0
    )


    fun setUser(id: Int){
        viewModelScope.launch { userRepository.setUser(id) }
    }


    val actions = object: UserActions{
        override fun changeUsername(name: String) {
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.changeUsername(name)
            }
        }
    }
}