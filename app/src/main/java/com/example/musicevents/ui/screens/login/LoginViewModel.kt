package com.example.musicevents.ui.screens.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.database.User
import com.example.musicevents.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//Data class
data class UserState(val users: List<User>)
data class UserLogged(val id: Int?)

//Action interface
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

    var userLogged by mutableStateOf(UserLogged(0))
        private set

    fun setUser(id: Int){
        userLogged = UserLogged(id)
        viewModelScope.launch { userRepository.setUser(id) }
    }

    init {
        viewModelScope.launch{
            userLogged = UserLogged(userRepository.id.first())
        }
    }

    val actions = object: LoginActions {

        override fun onLoginClick(email: String, pass: String, users: List<User>): Boolean {
            users.forEach{user ->
                if (user.email == email && user.password == pass){
                    setUser(user.id)
                    Log.d("USERLOGGED", userLogged.id.toString())
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