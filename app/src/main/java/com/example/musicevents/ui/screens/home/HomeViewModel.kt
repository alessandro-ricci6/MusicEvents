package com.example.musicevents.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.database.Event
import com.example.musicevents.data.repositories.EventsRepositories
import com.example.musicevents.ui.screens.login.LoginViewModel
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

interface HomeActions{
    fun saveEvent(event: Event)

    suspend fun isSaved(userId: Int, eventId: String): Boolean
}

class HomeViewModel(
    private val eventsRepositories: EventsRepositories,
    private val loginViewModel: LoginViewModel
): ViewModel() {

    val actions = object: HomeActions{
        override fun saveEvent(event: Event) {
            viewModelScope.launch {
                loginViewModel.userLogged.id?.let { eventsRepositories.userSaveEvent(it, event) }
            }
        }

        override suspend fun isSaved(userId: Int, eventId: String): Boolean {
            return false
        }
    }
}