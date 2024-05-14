package com.example.musicevents.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.database.User
import com.example.musicevents.data.database.UserSaveEvent
import com.example.musicevents.data.remote.EventApi
import com.example.musicevents.data.repositories.EventsRepositories
import com.example.musicevents.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class EventsState(val events: List<UserSaveEvent>)

interface ProfileActions {
    fun getUserById(userId: Int): User
}

class ProfileViewModel(
    private val eventsRepositories: EventsRepositories,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EventsState(emptyList()))
    init {
        viewModelScope.launch {
            val userId = userRepository.id.first()
            val eventsFlow = userRepository.getEventsOfUser(userId)
            val eventState = EventsState(eventsFlow.first())
            _state.value = eventState
        }
    }

    val state: StateFlow<EventsState> = _state.asStateFlow()



    val actions = object: ProfileActions {
        override fun getUserById(userId: Int): User {
            val result = runBlocking { userRepository.getUserFromId(userId) }
            return result
        }
    }

}