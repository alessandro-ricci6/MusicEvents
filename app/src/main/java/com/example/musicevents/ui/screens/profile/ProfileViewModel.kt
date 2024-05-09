package com.example.musicevents.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.database.Event
import com.example.musicevents.data.database.User
import com.example.musicevents.data.repositories.EventsRepositories
import com.example.musicevents.data.repositories.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

interface ProfileActions {
    fun getUserById(userId: Int): User
    fun getEventsOfUser(userId: Int) : List<Event>
}

class ProfileViewModel(
    private val eventsRepositories: EventsRepositories,
    private val userRepository: UserRepository
) : ViewModel() {

    val actions = object: ProfileActions {
        override fun getUserById(userId: Int): User {
            val result = runBlocking { userRepository.getUserFromId(userId) }
            return result
        }

        override fun getEventsOfUser(userId: Int): List<Event> {
            val result = runBlocking { eventsRepositories.getEventOfUser(userId) }
            return result
        }
    }
}