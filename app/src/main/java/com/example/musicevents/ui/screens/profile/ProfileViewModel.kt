package com.example.musicevents.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.database.User
import com.example.musicevents.data.repositories.EventsRepositories
import com.example.musicevents.data.repositories.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val eventsRepositories: EventsRepositories,
    private val userRepository: UserRepository
) : ViewModel() {
}