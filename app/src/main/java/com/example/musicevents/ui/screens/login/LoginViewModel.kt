package com.example.musicevents.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.database.User
import com.example.musicevents.data.repositories.UserRepository
import com.example.musicevents.ui.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//Data class
data class UserState(val users: List<User>)

//Action interface
interface LoginActions {
    fun onLoginClick(email: String, pass: String): Boolean
    fun onRegisterClick(name: String, pass: String, email: String): Boolean
}

class LoginViewModel (
    private val userRepository: UserRepository,
    private val userVm: UserViewModel
): ViewModel() {
    private val _state = MutableStateFlow(UserState(emptyList()))
    val state = _state.asStateFlow()


    init {
        viewModelScope.launch {
            _state.value = UserState(userRepository.users.first())
        }
    }


    val actions = object: LoginActions {

        override fun onLoginClick(email: String, pass: String): Boolean {
            state.value.users.forEach{user ->
                if (user.email == email && user.password == pass){
                    userVm.setUser(user.id)
                    return true
                }
            }
            return false
        }

        override fun onRegisterClick(name: String, pass: String, email: String):Boolean {
            state.value.users.forEach { user ->
                if(user.email == email || user.name == name) {
                    return false
                }
            }
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.upsert(User(name = name, password = pass, email = email, imageUri = ""))
                val id = userRepository.getIdFromEmail(email)
                userVm.setUser(id)
            }
            return true
        }

    }

}