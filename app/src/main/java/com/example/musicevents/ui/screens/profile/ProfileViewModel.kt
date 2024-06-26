package com.example.musicevents.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.database.User
import com.example.musicevents.data.database.UserSaveEvent
import com.example.musicevents.data.repositories.UserRepository
import com.example.musicevents.utils.InternetService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class ProfileState(
    val showNoInternetConnectivitySnackbar: Boolean = false,
    var events: List<UserSaveEvent>)

interface ProfileActions {
    fun getUserById(userId: Int): User
    fun setShowNoInternetConnectivitySnackbar(show: Boolean)
    fun isOnline(): Boolean
    fun openWirelessSettings()
}

class ProfileViewModel(
    private val internet: InternetService,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState(events = emptyList()))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val userId = userRepository.id.first()
            val eventsFlow = userRepository.getEventsOfUser(userId)
            _state.value.events = eventsFlow.first()
        }
    }




    val actions = object: ProfileActions {
        override fun getUserById(userId: Int): User {
            val result = runBlocking { userRepository.getUserFromId(userId) }
            return result
        }

        override fun isOnline(): Boolean {
            return internet.isOnline()
        }

        override fun openWirelessSettings() {
            internet.openWirelessSettings()
        }

        override fun setShowNoInternetConnectivitySnackbar(show: Boolean) =
            _state.update { it.copy(showNoInternetConnectivitySnackbar = show) }
    }

}