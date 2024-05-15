package com.example.musicevents.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.repositories.EventsRepositories
import com.example.musicevents.data.repositories.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

interface EventActions{
    fun saveEvent(eventId: String)
    fun isEventSaved(userId: Int, eventId: String): Boolean
    fun deleteSavedEvents(eventId: String)
}

class EventViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventsRepositories
): ViewModel(){
    val actions = object : EventActions {
        override fun saveEvent(eventId: String) {
            viewModelScope.launch {
                eventRepository.userSaveEvent(userRepository.id.first(), eventId)
            }
        }

        override fun isEventSaved(userId: Int, eventId: String): Boolean {
            val result = runBlocking { eventRepository.isEventSaved(userId, eventId) }
            Log.d("RESULT", result.toString())
            return result
        }

        override fun deleteSavedEvents(eventId: String) {
            viewModelScope.launch {
                val userId: Int = userRepository.id.first()
                eventRepository.deleteSavedEvent(userId, eventId)
            }
        }
    }
}