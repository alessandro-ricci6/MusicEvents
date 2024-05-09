package com.example.musicevents.ui.screens.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicevents.data.database.Event
import com.example.musicevents.data.repositories.EventsRepositories
import com.example.musicevents.ui.screens.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.internal.wait

data class HomeState(
    val showLocationDisabledAlert: Boolean = false,
    val showLocationPermissionDeniedAlert: Boolean = false,
    val showLocationPermissionPermanentlyDeniedSnackbar: Boolean = false,
)
interface HomeActions{
    fun saveEvent(event: Event)
    suspend fun isSaved(userId: Int, eventId: String): Boolean
    fun setShowLocationDisabledAlert(show: Boolean)
    fun setShowLocationPermissionDeniedAlert(show: Boolean)
    fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean)
    fun isEventSaved(userId: Int, eventId: String): Boolean
    fun deleteSavedEvents(eventId: String)
}

class HomeViewModel(
    private val eventsRepositories: EventsRepositories,
    private val loginViewModel: LoginViewModel
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    val actions = object: HomeActions{
        override fun saveEvent(event: Event) {
            viewModelScope.launch {
                eventsRepositories.upsert(event)
                loginViewModel.userLogged.id?.let { eventsRepositories.userSaveEvent(it, event.id) }
            }
        }

        override suspend fun isSaved(userId: Int, eventId: String): Boolean {
            return false
        }

        override fun setShowLocationDisabledAlert(show: Boolean) =
            _state.update { it.copy(showLocationDisabledAlert = show) }

        override fun setShowLocationPermissionDeniedAlert(show: Boolean) =
            _state.update { it.copy(showLocationPermissionDeniedAlert = show) }

        override fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean) =
            _state.update { it.copy(showLocationPermissionPermanentlyDeniedSnackbar = show) }

        override fun isEventSaved(userId: Int, eventId: String):Boolean {
            val result = runBlocking { eventsRepositories.isEventSaved(userId, eventId) }
            Log.d("RESULT", result.toString())
            return result
        }

        override fun deleteSavedEvents(eventId: String) {
            viewModelScope.launch {
                val userId: Int = loginViewModel.userLogged.id!!
                val id = eventsRepositories.getSavedEventId(userId, eventId)
                loginViewModel.userLogged.id?.let { eventsRepositories.deleteSavedEvent(id) }
                Log.d("SAVED", "ViewModel")
            }
        }
    }
}