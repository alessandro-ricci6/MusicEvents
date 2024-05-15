package com.example.musicevents.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.repositories.EventsRepositories
import com.example.musicevents.data.repositories.UserRepository
import com.example.musicevents.utils.InternetService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class HomeState(
    val showLocationDisabledAlert: Boolean = false,
    val showLocationPermissionDeniedAlert: Boolean = false,
    val showLocationPermissionPermanentlyDeniedSnackbar: Boolean = false,
    val showNoInternetConnectivitySnackbar: Boolean = false
)
interface HomeActions{
    fun saveEvent(eventId: String)
    suspend fun isSaved(userId: Int, eventId: String): Boolean
    fun setShowLocationDisabledAlert(show: Boolean)
    fun setShowLocationPermissionDeniedAlert(show: Boolean)
    fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean)
    fun setShowNoInternetConnectivitySnackbar(show: Boolean)
    fun isEventSaved(userId: Int, eventId: String): Boolean
    fun deleteSavedEvents(eventId: String)
    fun isOnline(): Boolean
    fun openWirelessSettings()
}

class HomeViewModel(
    private val eventsRepositories: EventsRepositories,
    private val userRepository: UserRepository,
    private val internet: InternetService
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    val actions = object: HomeActions{
        override fun saveEvent(eventId: String) {
            viewModelScope.launch {
                eventsRepositories.userSaveEvent(userRepository.id.first(), eventId)
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

        override fun setShowNoInternetConnectivitySnackbar(show: Boolean) =
            _state.update { it.copy(showNoInternetConnectivitySnackbar = show) }

        override fun isEventSaved(userId: Int, eventId: String):Boolean {
            val result = runBlocking { eventsRepositories.isEventSaved(userId, eventId) }
            Log.d("RESULT", result.toString())
            return result
        }

        override fun deleteSavedEvents(eventId: String) {
            viewModelScope.launch {
                val userId: Int = userRepository.id.first()
                eventsRepositories.deleteSavedEvent(userId, eventId)
            }
        }

        override fun isOnline(): Boolean {
            return internet.isOnline()
        }

        override fun openWirelessSettings() {
            internet.openWirelessSettings()
        }
    }
}