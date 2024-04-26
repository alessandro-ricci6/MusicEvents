package com.example.musicevents.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.database.User
import com.example.musicevents.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UserState(val users: List<User>)

interface LoginActions {
    fun onLoginClick(email: String, pass: String, users: List<User>): Boolean
    fun onRegisterClick(name: String, pass: String, email: String, users: List<User>): Boolean
}

class LoginViewModel (
    private val userRepository: UserRepository
): ViewModel() {
    var state = userRepository.users.map { UserState(users = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UserState(emptyList())
    )
    val actions = object: LoginActions {

        override fun onLoginClick(email: String, pass: String, users: List<User>): Boolean {
            users.forEach{user ->
                if (user.email == email && user.password == pass){
                    return true
                }
            }
            return false
        }

        override fun onRegisterClick(name: String, pass: String, email: String, users: List<User>):Boolean {
            users.forEach { user ->
                if(user.email == email || user.name == name) {
                    return false
                }
            }
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.upsert(User(name = name, password = pass, email = email))
            }
            return true
        }

    }

}